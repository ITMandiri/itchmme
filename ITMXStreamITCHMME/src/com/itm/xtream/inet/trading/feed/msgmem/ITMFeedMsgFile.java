/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.msgmem;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupConsts;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Penyimpan datafeed (hasil olahan ITCH / ITCH MDF) berbasis file, pengganti ArrayList&lt;String&gt; di memori.
 *
 * Aturan:
 * - Satu file per tanggal: datafeed_yyyyMMdd.mem, isinya satu baris per sequence (sequence 1 = baris pertama).
 * - File bersifat append-only dan TIDAK dihapus saat aplikasi restart. Saat start, file dipindai untuk
 *   membangun indeks dan mengetahui sequence terakhir yang sudah tersimpan (size()).
 * - Indeks di memori hanya menyimpan offset byte untuk setiap BLOCK_SIZE baris (hemat memori untuk puluhan juta baris).
 * - Baris terbaru disimpan juga di ring buffer di memori, sehingga client yang sudah "live" tidak perlu membaca disk.
 *
 * Penulisan ke disk (non-blocking untuk pemanggil append):
 * - append() hanya menyalin ke buffer aktif di memori. Jika buffer penuh (atau sudah waktunya flush), buffer
 *   diserahkan ke antrean dan thread penulis khusus ("ITMFeedMsgFile-Writer") yang menulisnya ke disk.
 * - Thread penulis menulis TANPA memegang kunci milik append(), sehingga I/O disk tidak pernah menahan append().
 * - Antrean tulis di memori dibatasi MAX_PENDING_BYTES (default 1GB) agar penulisan yang tertinggal pada saat data
 *   sangat ramai (burst) tertampung di memori, bukan menahan append(). Data TIDAK dibuang. Hanya jika antrean
 *   benar-benar penuh (disk jauh tertinggal), append() menunggu penulis menguras antrean (dicatat sebagai error);
 *   jika setelah MAX_WAIT_FOR_SPACE_MS masih penuh (misalnya disk rusak), baris dibuang agar aplikasi tidak hang
 *   dan append() mengembalikan false (pemanggil membatalkan nomor sequence-nya).
 *
 * @author fredy
 */
public class ITMFeedMsgFile {

    private final static Charset CHARSET                = Charset.forName("UTF-8");
    private final static String FILE_PREFIX             = "datafeed_";
    private final static String FILE_EXTENSION          = ".mem";

    private final static int BLOCK_SHIFT                = 6;                    //.64 baris per blok indeks.
    private final static int BLOCK_SIZE                 = (1 << BLOCK_SHIFT);
    private final static int BLOCK_MASK                 = (BLOCK_SIZE - 1);

    private final static int RING_SIZE                  = (1 << 16);            //.65536 baris terakhir tersimpan di memori.
    private final static int RING_MASK                  = (RING_SIZE - 1);
    private final static int RING_SAFE_MARGIN           = 16;                   //.jarak aman agar tidak membaca slot yang sedang ditimpa.

    private final static int WRITE_BUFFER_SIZE          = (1 << 16);            //.64KB per buffer tulis.
    private final static int READ_CHUNK_SIZE            = (1 << 16);            //.64KB.
    private final static long WRITER_IDLE_INTERVAL_MS   = 200;                  //.maksimum jeda sebelum buffer yang belum penuh diserahkan ke penulis.
    private final static long WRITER_RETRY_DELAY_MS     = 1000;                 //.jeda sebelum mencoba menulis ulang jika disk error.
    private final static long DEFAULT_MAX_PENDING_BYTES = (1024L << 20);        //.1GB antrean tulis di memori (perlu -Xmx yang cukup).
    private final static long MAX_WAIT_FOR_SPACE_MS     = 60000;                //.batas menunggu antrean berkurang sebelum baris dibuang.

    //.harus dideklarasikan SETELAH konstanta di atas (urutan inisialisasi static), karena konstruktor memakainya.
    public final static ITMFeedMsgFile getInstance = new ITMFeedMsgFile();

    /** Satu unit tulis ke disk. */
    private final static class WriteBlock {
        final byte[] arrData;
        final int iLength;
        final int iLineCountAfter;  //.jumlah baris total di file setelah blok ini tertulis.
        WriteBlock(byte[] arrData, int iLength, int iLineCountAfter){
            this.arrData = arrData;
            this.iLength = iLength;
            this.iLineCountAfter = iLineCountAfter;
        }
    }

    //.lockWriter: melindungi state penambahan (buffer aktif, iCount, ring, indeks, logical size). Dipegang SANGAT singkat.
    private final Object lockWriter                     = new Object();
    //.lockDisk: melindungi penulisan ke disk dan pergantian file. TIDAK PERNAH diminta oleh append().
    //.Urutan pengambilan kunci jika bersarang: lockDisk -> lockWriter.
    private final Object lockDisk                       = new Object();
    private final Object lockWake                       = new Object();
    //.lockData: tempat pembaca (thread kirim ke client) menunggu data baru.
    private final Object lockData                       = new Object();
    private final AtomicInteger aiWaiters               = new AtomicInteger(0);
    private volatile long lSignalVersion                = 0;                    //.hanya diubah di dalam lockData.

    private final String zDirectory;
    private final long lMaxPendingBytes;

    //.file yang sedang aktif (diubah hanya di dalam lockDisk + lockWriter):
    private String zCurrentDate                         = "";
    private RandomAccessFile rafFile                    = null;
    private volatile FileChannel fcFile                 = null;

    //.indeks: blockOffsets[k] = offset byte awal baris ke (k * BLOCK_SIZE), 0-based.
    private volatile long[] blockOffsets                = new long[1024];

    //.iCount = jumlah baris yang sudah diterima append() (= sequence terakhir); iFlushedCount = yang sudah tertulis di file.
    private volatile int iCount                         = 0;
    private volatile int iFlushedCount                  = 0;
    private volatile long lFlushedSize                  = 0;                    //.ukuran file yang sudah tertulis (hanya diubah penulis).
    private long lLogicalSize                           = 0;                    //.ukuran file + isi antrean + buffer aktif (hanya di lockWriter).

    private byte[] bufActive                            = new byte[WRITE_BUFFER_SIZE];
    private int iBufActivePos                           = 0;

    private final ConcurrentLinkedQueue<WriteBlock> clqPending = new ConcurrentLinkedQueue<WriteBlock>();
    private final AtomicLong alPendingBytes             = new AtomicLong(0);

    private final String[] ring                         = new String[RING_SIZE];

    private long tLastDropLog                           = 0;
    private int iDroppedLines                           = 0;

    //.hanya untuk pengujian: menahan penulis agar bisa menguji antrean penuh.
    volatile boolean bTestPauseWriter                   = false;

    private ITMFeedMsgFile(){
        this(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_DIRECTORY, DEFAULT_MAX_PENDING_BYTES);
    }

    //.package-private, untuk pengujian.
    ITMFeedMsgFile(String zDirectory, long lMaxPendingBytes){
        this.zDirectory = zDirectory;
        this.lMaxPendingBytes = lMaxPendingBytes;
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
        synchronized(lockDisk){
            synchronized(lockWriter){
                openForDate(getTodayDate());
            }
        }
        startWriter();
    }

    //.===================================== API =====================================

    /**
     * @return jumlah baris yang sudah diterima (= sequence terakhir yang tersimpan / akan tersimpan).
     */
    public int size(){
        return this.iCount;
    }

    /**
     * Tambah satu baris di akhir. Sequence baris ini adalah size() + 1 (sebelum dipanggil).
     * Tidak pernah menunggu I/O disk.
     * @return true jika diterima; false jika gagal / antrean tulis penuh (baris tidak disimpan, size() tidak berubah).
     */
    public boolean append(String zLine){
        if (zLine == null){
            zLine = "";
        }
        //.satu baris = satu record, karakter pemisah baris tidak boleh ada di dalam isi:
        if ((zLine.indexOf('\n') >= 0) || (zLine.indexOf('\r') >= 0)){
            zLine = zLine.replace('\r', ' ').replace('\n', ' ');
        }
        byte[] arrBytes = (zLine + "\n").getBytes(CHARSET);
        boolean bWake = false;
        //.kasus ekstrem: antrean tulis di memori penuh. Tunggu DI LUAR kunci (penulis perlu lockWriter untuk menguras).
        if (this.alPendingBytes.get() > this.lMaxPendingBytes){
            if (!waitForQueueSpace()){
                synchronized(lockWriter){
                    noteDroppedLine();
                }
                return false;
            }
        }
        synchronized(lockWriter){
            try{
                if (this.fcFile == null){
                    return false;
                }
                if (arrBytes.length > (WRITE_BUFFER_SIZE - this.iBufActivePos)){
                    handoffActiveLocked();
                    bWake = true;
                }
                int iLineIndex = this.iCount; //.0-based.
                if ((iLineIndex & BLOCK_MASK) == 0){
                    setBlockOffset((iLineIndex >> BLOCK_SHIFT), this.lLogicalSize);
                }
                this.ring[iLineIndex & RING_MASK] = zLine;
                this.lLogicalSize += arrBytes.length;
                if (arrBytes.length > WRITE_BUFFER_SIZE){
                    //.baris sangat besar: jadikan satu blok sendiri (buffer aktif sudah kosong karena handoff di atas).
                    this.iCount = (iLineIndex + 1);
                    this.alPendingBytes.addAndGet(arrBytes.length);
                    this.clqPending.add(new WriteBlock(arrBytes, arrBytes.length, this.iCount));
                    bWake = true;
                }else{
                    System.arraycopy(arrBytes, 0, this.bufActive, this.iBufActivePos, arrBytes.length);
                    this.iBufActivePos += arrBytes.length;
                    this.iCount = (iLineIndex + 1); //.volatile write terakhir: pembaca baru melihat baris setelah ini.
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                return false;
            }
        }
        if (bWake){
            wakeWriter();
        }
        //.beritahu pembaca (thread kirim ke client) yang sedang menunggu data baru. Hanya mengambil kunci jika ada yang menunggu:
        if (this.aiWaiters.get() > 0){
            synchronized(lockData){
                lockData.notifyAll();
            }
        }
        return true;
    }

    /**
     * Nilai versi sinyal bangun-paksa. Dibaca SEBELUM memeriksa kondisi, lalu diberikan ke awaitNewData().
     */
    public long getSignalVersion(){
        return this.lSignalVersion;
    }

    /**
     * Bangunkan semua pembaca yang sedang menunggu di awaitNewData() (misalnya karena ada perubahan sequence
     * permintaan client atau processor harus berhenti), walaupun belum ada baris baru.
     */
    public void wakeWaiters(){
        synchronized(lockData){
            this.lSignalVersion++;
            lockData.notifyAll();
        }
    }

    /**
     * Tunggu sampai ada baris dengan sequence iNextSeq (atau lebih), atau wakeWaiters() dipanggil setelah
     * lVersion diambil, atau lTimeoutMs habis. Tidak ada wakeup yang terlewat: pemeriksaan dan penungguan dilakukan
     * atomik terhadap monitor yang sama dengan pemberitahuannya.
     * @param lVersion nilai getSignalVersion() yang diambil SEBELUM pemanggil memeriksa kondisinya sendiri.
     * @param iNextSeq sequence yang ingin dikirim berikutnya.
     */
    public void awaitNewData(long lVersion, int iNextSeq, long lTimeoutMs) throws InterruptedException {
        if (lTimeoutMs <= 0){
            return;
        }
        this.aiWaiters.incrementAndGet();
        try{
            synchronized(lockData){
                if ((this.lSignalVersion == lVersion) && (this.iCount < iNextSeq)){
                    lockData.wait(lTimeoutMs);
                }
            }
        }finally{
            this.aiWaiters.decrementAndGet();
        }
    }

    /**
     * Ambil satu baris berdasarkan sequence (mulai 1).
     * @return baris, atau null jika sequence di luar jangkauan / belum bisa dibaca.
     */
    public String get(int iSequence){
        List<String> lst = getRange(iSequence, 1);
        if (lst.isEmpty()){
            return null;
        }
        return lst.get(0);
    }

    /**
     * Ambil beberapa baris berurutan mulai dari iFromSequence (mulai 1). Lebih efisien daripada get() berulang.
     * Tidak pernah memblokir menunggu penulisan: jika baris yang diminta sudah tidak ada di ring buffer dan
     * belum sampai ke file, hasilnya kosong / lebih pendek, dan pemanggil mencoba lagi nanti.
     * @return daftar baris (bisa lebih sedikit dari iMaxLines, kosong jika di luar jangkauan / belum tersedia).
     */
    public List<String> getRange(int iFromSequence, int iMaxLines){
        int iSnapCount = this.iCount;
        if ((iFromSequence < 1) || (iFromSequence > iSnapCount) || (iMaxLines < 1)){
            return new ArrayList<String>(0);
        }
        long lTo = Math.min((long)iSnapCount, (long)iFromSequence + iMaxLines - 1);
        int iTo = (int)lTo;

        //.1. coba dari ring buffer (tanpa kunci):
        if (iFromSequence > (iSnapCount - RING_SIZE + RING_SAFE_MARGIN)){
            ArrayList<String> lstRing = new ArrayList<String>(iTo - iFromSequence + 1);
            for (int s = iFromSequence; s <= iTo; s++){
                lstRing.add(this.ring[(s - 1) & RING_MASK]);
            }
            int iSnapCount2 = this.iCount;
            if (iFromSequence > (iSnapCount2 - RING_SIZE + RING_SAFE_MARGIN)){
                return lstRing; //.slot belum tertimpa penulis.
            }
        }

        //.2. baca dari file (hanya bagian yang sudah tertulis):
        try{
            int iFlushed = this.iFlushedCount;
            if (iFromSequence > iFlushed){
                return new ArrayList<String>(0);
            }
            return readFromFile(iFromSequence, Math.min(iTo, iFlushed));
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return new ArrayList<String>(0);
    }

    /**
     * Tulis semua yang tertunda ke disk dan tunggu sampai selesai (sinkron). Untuk shutdown dan pergantian file;
     * tidak dipanggil dari jalur append().
     */
    public void flush(){
        synchronized(lockDisk){
            synchronized(lockWriter){
                handoffActiveLocked();
            }
            drainPendingLocked();
        }
    }

    /**
     * Pastikan file yang aktif adalah file untuk tanggal hari ini. Dipanggil saat awal hari (reset data).
     * Isi file TIDAK dihapus. Jika tanggal sama dengan file yang sedang aktif, tidak melakukan apa-apa.
     */
    public void switchToTodayFile(){
        synchronized(lockDisk){
            String zToday = getTodayDate();
            if (!zToday.equals(this.zCurrentDate)){
                synchronized(lockWriter){
                    openForDate(zToday);
                }
            }
        }
    }

    //.===================================== internal =====================================

    private static String getTodayDate(){
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    private File getFileForDate(String zDate){
        return new File(this.zDirectory + FILE_PREFIX + zDate + FILE_EXTENSION);
    }

    /**
     * Tunggu penulis menguras antrean sampai di bawah batas. Hanya terjadi jika disk sangat tertinggal.
     * @return true jika sudah ada ruang; false jika batas waktu tunggu terlewati.
     */
    private boolean waitForQueueSpace(){
        long tStart = System.currentTimeMillis();
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "feed file write queue full (" + this.alPendingBytes.get() + " bytes), waiting for disk writer ... .");
        wakeWriter();
        while (this.alPendingBytes.get() > this.lMaxPendingBytes){
            if ((System.currentTimeMillis() - tStart) > MAX_WAIT_FOR_SPACE_MS){
                return false;
            }
            try{
                Thread.sleep(1);
            }catch(InterruptedException ex0){
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    //.dipanggil di dalam lockWriter.
    private void noteDroppedLine(){
        this.iDroppedLines++;
        long tNow = System.currentTimeMillis();
        if ((tNow - this.tLastDropLog) >= 5000){
            this.tLastDropLog = tNow;
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "feed file write queue full (disk too slow?), lines dropped so far: " + this.iDroppedLines + " > pending bytes: " + this.alPendingBytes.get());
        }
    }

    private void setBlockOffset(int iBlock, long lOffset){
        long[] arr = this.blockOffsets;
        if (iBlock >= arr.length){
            arr = Arrays.copyOf(arr, Math.max(arr.length * 2, iBlock + 1));
            this.blockOffsets = arr;
        }
        arr[iBlock] = lOffset;
    }

    private void wakeWriter(){
        synchronized(lockWake){
            lockWake.notify();
        }
    }

    /**
     * Serahkan buffer aktif ke antrean tulis dan siapkan buffer baru. Dipanggil di dalam lockWriter.
     */
    private void handoffActiveLocked(){
        if (this.iBufActivePos > 0){
            this.alPendingBytes.addAndGet(this.iBufActivePos);
            this.clqPending.add(new WriteBlock(this.bufActive, this.iBufActivePos, this.iCount));
            this.bufActive = new byte[WRITE_BUFFER_SIZE];
            this.iBufActivePos = 0;
        }
    }

    /**
     * Tulis semua blok di antrean ke file secara berurutan. Dipanggil di dalam lockDisk.
     * Jika gagal menulis, blok dibiarkan di antrean dan dicoba lagi di putaran berikutnya (offset tetap konsisten).
     * @return true jika antrean kosong setelahnya.
     */
    private boolean drainPendingLocked(){
        FileChannel fc = this.fcFile;
        WriteBlock wb;
        while ((wb = this.clqPending.peek()) != null){
            if (fc == null){
                return false;
            }
            try{
                ByteBuffer bb = ByteBuffer.wrap(wb.arrData, 0, wb.iLength);
                long lPos = this.lFlushedSize;
                while (bb.hasRemaining()){
                    lPos += fc.write(bb, lPos);
                }
                this.lFlushedSize = lPos;
                this.iFlushedCount = wb.iLineCountAfter; //.setelah lFlushedSize: pembaca file memakai keduanya.
                this.clqPending.poll();
                this.alPendingBytes.addAndGet(-wb.iLength);
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                return false;
            }
        }
        return true;
    }

    /**
     * Tutup file yang sedang terbuka. Dipanggil di dalam lockDisk + lockWriter.
     */
    private void closeCurrentLocked(){
        handoffActiveLocked();
        drainPendingLocked();
        try{
            if (this.fcFile != null){
                this.fcFile.close();
            }
            if (this.rafFile != null){
                this.rafFile.close();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        this.fcFile = null;
        this.rafFile = null;
    }

    /**
     * Buka (atau buat) file untuk tanggal tertentu, lalu pindai isinya untuk membangun indeks.
     * Harus dipanggil di dalam lockDisk + lockWriter.
     */
    private void openForDate(String zDate){
        closeCurrentLocked();
        //.reset keadaan (antrean sudah kosong setelah closeCurrentLocked, kecuali disk error):
        this.clqPending.clear();
        this.alPendingBytes.set(0);
        this.blockOffsets = new long[1024];
        this.iCount = 0;
        this.iFlushedCount = 0;
        this.lFlushedSize = 0;
        this.lLogicalSize = 0;
        this.iBufActivePos = 0;
        Arrays.fill(this.ring, null);
        this.zCurrentDate = zDate;
        try{
            File fDir = new File(this.zDirectory);
            if (!fDir.exists()){
                fDir.mkdirs();
            }
            File fFile = getFileForDate(zDate);
            this.rafFile = new RandomAccessFile(fFile, "rw");
            this.fcFile = this.rafFile.getChannel();
            scanExistingFile();
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "feed file opened:" + fFile.getPath() + " > lines:" + this.iCount + " > bytes:" + this.lFlushedSize);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            this.fcFile = null;
            this.rafFile = null;
        }
    }

    /**
     * Pindai seluruh file: hitung jumlah baris, bangun indeks blok, dan isi ring buffer dengan baris terakhir.
     * Jika ada baris terakhir yang tidak lengkap (tanpa '\n', misalnya karena aplikasi mati saat menulis), dipotong.
     */
    private void scanExistingFile() throws IOException {
        FileChannel fc = this.fcFile;
        long lFileSize = fc.size();
        long lPos = 0;
        long lLineStart = 0;
        long lLastCompleteEnd = 0;
        int iLines = 0;
        ByteBuffer bb = ByteBuffer.allocate(1 << 20);
        while (lPos < lFileSize){
            bb.clear();
            int iRead = fc.read(bb, lPos);
            if (iRead <= 0){
                break;
            }
            byte[] arr = bb.array();
            for (int i = 0; i < iRead; i++){
                if (arr[i] == (byte)'\n'){
                    long lNext = lPos + i + 1;
                    if ((iLines & BLOCK_MASK) == 0){
                        setBlockOffset((iLines >> BLOCK_SHIFT), lLineStart);
                    }
                    iLines++;
                    lLineStart = lNext;
                    lLastCompleteEnd = lNext;
                }
            }
            lPos += iRead;
        }
        if (lLastCompleteEnd < lFileSize){
            fc.truncate(lLastCompleteEnd);
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "feed file incomplete last line truncated: " + (lFileSize - lLastCompleteEnd) + " bytes");
        }
        this.lFlushedSize = lLastCompleteEnd;
        this.lLogicalSize = lLastCompleteEnd;
        this.iCount = iLines;
        this.iFlushedCount = iLines;
        //.isi ring dengan baris terakhir agar client live langsung terlayani dari memori:
        if (iLines > 0){
            int iRingFrom = Math.max(1, iLines - RING_SIZE + RING_SAFE_MARGIN + 1);
            List<String> lstTail = readFromFile(iRingFrom, iLines);
            for (int i = 0; i < lstTail.size(); i++){
                this.ring[(iRingFrom - 1 + i) & RING_MASK] = lstTail.get(i);
            }
        }
    }

    /**
     * Baca baris iFrom..iTo (1-based, inklusif) langsung dari file. Hanya boleh meminta baris yang sudah tertulis.
     */
    private List<String> readFromFile(int iFrom, int iTo) throws IOException {
        FileChannel fc = this.fcFile;
        ArrayList<String> lstOut = new ArrayList<String>(Math.max(0, iTo - iFrom + 1));
        if (fc == null){
            return lstOut;
        }
        long[] arrOffsets = this.blockOffsets;
        int iBlock = ((iFrom - 1) >> BLOCK_SHIFT);
        if (iBlock >= arrOffsets.length){
            return lstOut;
        }
        long lPos = arrOffsets[iBlock];
        long lLimit = this.lFlushedSize;
        int iSkip = ((iFrom - 1) & BLOCK_MASK);
        int iNeed = (iTo - iFrom + 1);

        ByteBuffer bb = ByteBuffer.allocate(READ_CHUNK_SIZE);
        byte[] arrLine = new byte[256];
        int iLineLen = 0;
        while ((iNeed > 0) && (lPos < lLimit)){
            bb.clear();
            int iToRead = (int)Math.min((long)READ_CHUNK_SIZE, lLimit - lPos);
            bb.limit(iToRead);
            int iRead = fc.read(bb, lPos);
            if (iRead <= 0){
                break;
            }
            byte[] arr = bb.array();
            int iSegStart = 0;
            for (int i = 0; i < iRead && iNeed > 0; i++){
                if (arr[i] == (byte)'\n'){
                    int iSegLen = (i - iSegStart);
                    if (iSkip > 0){
                        iSkip--;
                    }else{
                        String zLine;
                        if (iLineLen == 0){
                            zLine = new String(arr, iSegStart, iSegLen, CHARSET);
                        }else{
                            if (iLineLen + iSegLen > arrLine.length){
                                arrLine = Arrays.copyOf(arrLine, Math.max(arrLine.length * 2, iLineLen + iSegLen));
                            }
                            System.arraycopy(arr, iSegStart, arrLine, iLineLen, iSegLen);
                            zLine = new String(arrLine, 0, iLineLen + iSegLen, CHARSET);
                        }
                        lstOut.add(zLine);
                        iNeed--;
                    }
                    iLineLen = 0;
                    iSegStart = i + 1;
                }
            }
            //.sisa potongan baris di akhir chunk (baris yang melewati batas chunk):
            if ((iNeed > 0) && (iSegStart < iRead)){
                int iRest = (iRead - iSegStart);
                if (iLineLen + iRest > arrLine.length){
                    arrLine = Arrays.copyOf(arrLine, Math.max(arrLine.length * 2, iLineLen + iRest));
                }
                System.arraycopy(arr, iSegStart, arrLine, iLineLen, iRest);
                iLineLen += iRest;
            }
            lPos += iRead;
        }
        return lstOut;
    }

    /**
     * Thread penulis khusus: menunggu blok dari append() (atau jeda WRITER_IDLE_INTERVAL_MS),
     * menyerahkan buffer yang belum penuh, lalu menulis semuanya ke disk.
     */
    private void startWriter(){
        Thread th = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try{
                        synchronized(lockWake){
                            if (clqPending.isEmpty()){
                                lockWake.wait(WRITER_IDLE_INTERVAL_MS);
                            }
                        }
                        if (bTestPauseWriter){
                            Thread.sleep(10);
                            continue;
                        }
                        //.serahkan buffer yang belum penuh (sebentar saja memegang lockWriter):
                        synchronized(lockWriter){
                            handoffActiveLocked();
                        }
                        //.tulis ke disk TANPA lockWriter:
                        boolean bAllWritten;
                        synchronized(lockDisk){
                            bAllWritten = drainPendingLocked();
                        }
                        if (!bAllWritten){
                            Thread.sleep(WRITER_RETRY_DELAY_MS); //.disk error: jangan memutar CPU.
                        }
                    }catch(InterruptedException ex0){
                        break;
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                    }
                }
            }
        }, "ITMFeedMsgFile-Writer");
        th.setDaemon(true);
        th.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
            @Override
            public void run() {
                bTestPauseWriter = false;
                flush();
            }
        }, "ITMFeedMsgFile-ShutdownFlush"));
    }

}
