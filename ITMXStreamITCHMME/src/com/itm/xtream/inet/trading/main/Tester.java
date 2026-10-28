package com.itm.xtream.inet.trading.main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tester {
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\fredy\\Kerja\\ITM\\PSPP\\LS\\itch\\soupbintcp_backups\\ITCH_MDF_DUMP_20261024.bak";
        String outputFile = "C:\\Users\\fredy\\Kerja\\ITM\\PSPP\\LS\\itch\\soupbintcp_backups\\ITCH_MDF_RECV_20261027.bak";

        // Mendapatkan tanggal hari ini dengan format yyyy-MM-dd menggunakan SimpleDateFormat (Java 1.7)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        // Menggunakan try-with-resources (tersedia sejak Java 1.7)
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Memisahkan baris berdasarkan karakter '|'
                String[] parts = line.split("\\|", -1);

                // Pastikan panjang array cukup dan index ke-3 adalah 'I'
                if (parts.length >= 4 && "I".equals(parts[3])) {
                    // Ganti tanggal pada index ke-1 dengan tanggal sekarang
                    parts[1] = currentDate;

                    // Menggabungkan kembali array menjadi satu baris string menggunakan StringBuilder (Java 1.7 fallback)
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        sb.append(parts[i]);
                        if (i < parts.length - 1) {
                            sb.append("|");
                        }
                    }

                    // Tulis ke file output
                    writer.write(sb.toString());
                    writer.newLine();
                }
            }
            
            System.out.println("Proses selesai! Baris 'O' telah dihapus dan tanggal telah diperbarui.");
            System.out.println("Silakan cek file: " + outputFile);

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat memproses file: " + e.getMessage());
        }
    }
}