/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.helpers;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValue;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValueLength;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5CheckSumHelper {
    
    public static String fixNegDealTradeReportID(String zTradeReportID, boolean bToExpand){
        String zOut = zTradeReportID; //.expand="20210314-000001767711"; collapse="210314000001767711";
        try{
            if (!StringHelper.isNullOrEmpty(zOut)){
                String zPrefix = "20";
                if (bToExpand){
                    if (!zOut.startsWith(zPrefix)){
                        zOut = (zPrefix + zOut);
                    }
                    if ((!zOut.contains("-")) && (zOut.length() >= 10)){
                        zOut = zOut.substring(0, 8) + "-" + zOut.substring(8);
                    }
                }else{
                    if (zOut.startsWith(zPrefix)){
                        zOut = zOut.substring(2);
                    }
                    zOut = zOut.replaceAll("-", "");
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String repackMessageWithChecksum(String zFIX5IDXMessage, boolean bReCalculateBodyLength, boolean bAppendIdxFixEngineTags, String zFIX5ConnectionName){
        String zOut = zFIX5IDXMessage;
        try{
            if (!StringHelper.isNullOrEmpty(zOut)){
                String zOnlyMsg = zOut;
                if (zOnlyMsg.contains(FIX5JonecFieldFmt.FIND_LAST_TAG)){
                    zOnlyMsg = zOnlyMsg.substring(0, zOnlyMsg.indexOf(FIX5JonecFieldFmt.FIND_LAST_TAG) + 1);
                }
                if (!zOnlyMsg.endsWith(FIX5JonecFieldFmt.FIELD_SEPARATOR)){
                    zOnlyMsg += FIX5JonecFieldFmt.FIELD_SEPARATOR;
                }
                if (!StringHelper.isNullOrEmpty(zOnlyMsg)){
                    if (bReCalculateBodyLength){
                        int pBodyPositionA = zOnlyMsg.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.BODYLENGTH + FIX5JonecFieldFmt.KV_SEPARATOR);
                        int pBodyPositionB = 0;
                        int vBodyLength = 0;
                        if (pBodyPositionA > 0){
                            pBodyPositionA += (FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.BODYLENGTH + FIX5JonecFieldFmt.KV_SEPARATOR).length();
                            pBodyPositionB = zOnlyMsg.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR, pBodyPositionA);
                            if (pBodyPositionB > 0){
                                vBodyLength = (zOnlyMsg.length() - pBodyPositionB - 1);
                                if (vBodyLength > 0){
                                    zOnlyMsg = (zOnlyMsg.substring(0, pBodyPositionA) + StringHelper.addZeroFromInt(vBodyLength, FIX5JonecFieldValueLength.BODYLENGTH) + zOnlyMsg.substring(pBodyPositionB));
                                }
                            }
                        }
                    }
                    int vCheckSum = 0;
                    int vSumValue = 0;
                    byte[] btMsg = zOnlyMsg.getBytes();
                    if (btMsg.length > 0){
                        for(byte btd : btMsg){
                            vSumValue += (btd & 0xff);
                        }
                    }
                    vCheckSum = (vSumValue % 256);
                    zOut = zOnlyMsg + FIX5JonecFieldTag.CHECKSUM + FIX5JonecFieldFmt.KV_SEPARATOR + StringHelper.addZeroFromInt(vCheckSum, FIX5JonecFieldValueLength.CHECKSUM) + FIX5JonecFieldFmt.FIELD_SEPARATOR;
                }
                
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        if (bAppendIdxFixEngineTags){
            try{
                String zMsgType = "";
                if (StringHelper.isNullOrEmpty(zOut)){
                    zOut = "";
                }else{
                    int pStartMsgType = zOut.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.MSGTYPE + FIX5JonecFieldFmt.KV_SEPARATOR);
                    if (pStartMsgType > 0){
                        pStartMsgType += (FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.MSGTYPE + FIX5JonecFieldFmt.KV_SEPARATOR).length();
                        int pStopMsgType = zOut.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR, pStartMsgType);
                        if (pStopMsgType > 0 && pStopMsgType > pStartMsgType){
                            zMsgType = zOut.substring(pStartMsgType, pStopMsgType);
                        }
                    }
                }
                if (StringHelper.isNullOrEmpty(zFIX5ConnectionName)){
                    zFIX5ConnectionName = FIX5JonecFieldValue.IDXFIX_DEFAULT_CONNECTION_NAME;
                }else if (zFIX5ConnectionName.startsWith(FIX5JonecFieldValue.IDXFIX_CONNECTION_NAME_PREFIX)){
                    zFIX5ConnectionName = zFIX5ConnectionName.substring(FIX5JonecFieldValue.IDXFIX_CONNECTION_NAME_PREFIX.length());
                }
                zOut = FIX5JonecFieldValue.IDXFIX_DEFAULT_PREFIX 
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + zMsgType
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + zFIX5ConnectionName
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + "0"
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + "N"
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + zOut
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                        + "0"
                        + FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR
                ;
            }catch(Exception ex0){
                //.EXXX.
            }
        }
        return zOut;
    }
    
    public static int fixXmlToClassGenerator(String zFixXmlFilePath, String zClassOutputDir){
        int mOut = 0;
        try{
            File flXmlFile = new File(zFixXmlFilePath);
            File flOutDir = new File(zClassOutputDir);
            if ((flXmlFile.exists() && flXmlFile.isFile()) && (flOutDir.exists() && flOutDir.isDirectory())){
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(flXmlFile);
                if (document != null){
                    document.getDocumentElement().normalize();
                    System.out.println("Root element :" + document.getDocumentElement().getNodeName());
                    NodeList nMsgs = document.getElementsByTagName("message");
                    if (nMsgs != null && nMsgs.getLength() > 0){
                        //System.out.println("Messages element :" + nMessages..getNodeName());
                        for(int pm = 0; pm < nMsgs.getLength(); pm++){
                            Node nMsg = nMsgs.item(pm);
                            Element eMsg = (Element)nMsg;
                            System.out.println("Msg element(" + pm + ") :" + nMsg.getNodeName() + "|" + nMsg.getNodeType() + "|" + eMsg.getAttribute("name"));
                            String zClassName = "FIX5JonecData" + eMsg.getAttribute("name");
                            if (zClassName.indexOf("(") > 0){
                                zClassName = zClassName.substring(0, zClassName.indexOf("("));
                            }
                            zClassName = zClassName.replaceAll(" ", "").replaceAll("/", "").replaceAll("-", "").replaceAll("_", "");
                            System.out.println("ClassName=" + zClassName);
                            String zOutFilePath = zClassOutputDir + "\\" + zClassName + ".java";
                            File flOutFile = new File(zOutFilePath);
                            if (flOutFile.isFile() && flOutFile.exists()){
                                ///flOutFile.renameTo(new File(zOutFilePath + "_" + System.currentTimeMillis() + ".bak"));
                                flOutFile.delete();
                                flOutFile = new File(zOutFilePath);
                            }
                            flOutFile.createNewFile();
                            if (flOutFile.exists()){
                                String zFileContent = "/*\n" +
                                " * To change this template, choose Tools | Templates\n" +
                                " * and open the template in the editor.\n" +
                                " */\n" +
                                "package com.itm.fix5.data.jonec.message.struct;\n\n" +
                                "import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;\n" +
                                "import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;\n" +
                                "import com.itm.generic.engine.socket.uhelpers.StringHelper;\n" +
                                "import java.util.Map;\n" +
                                "\n\n";
                                zFileContent += "/**\n" +
                                " *\n" +
                                " * @author aripam\n" +
                                " */\n" +
                                "public class " + zClassName + " extends FIX5JonecDataHeader {\n" +
                                "\n" +
                                "    //.reference name: " + eMsg.getAttribute("name") + " (" + eMsg.getAttribute("msgtype") + ")" + "\n" +
                                "\n" +
                                "    //.fields:\n";
                                String zFxGetSet = "";
                                String zFxAssign = "";
                                String zFxMsgToStr = "";
                                NodeList nFields = eMsg.getElementsByTagName("field");
                                if (nFields != null && nFields.getLength() > 0){
                                    for(int pf = 0; pf < nFields.getLength(); pf++){
                                        Node nField = nFields.item(pf);
                                        Element eField = (Element)nField;
                                        String zFieldDataType = "";
                                        String zFieldName = eField.getAttribute("name");
                                        if (StringHelper.isNullOrEmpty(zFieldDataType)){
                                            zFieldDataType = "String";
                                        }
                                        zFileContent += "    private " + zFieldDataType + " f" + zFieldName + " = \"\";\n";
                                        zFxGetSet += "\n" +
                                        "    public " + zFieldDataType + " getf" + zFieldName + "() {\n" +
                                        "        return f" + zFieldName + ";\n" +
                                        "    }\n" +
                                        "\n" +
                                        "    public void setf" + zFieldName + "(" + zFieldDataType + " f" + zFieldName + ") {\n" +
                                        "        this.f" + zFieldName + " = f" + zFieldName + ";\n" +
                                        "    }\n" +
                                        "    ";
                                        zFxAssign += "                        case FIX5JonecFieldTag." + zFieldName.toUpperCase() + ":\n" +
                                        "                            setf" + zFieldName + "(zValue);\n" +
                                        "                            break;\n";
                                        zFxMsgToStr += "            sb.append(FIX5JonecFieldTag." + zFieldName.toUpperCase() + ").append(FIX5JonecFieldFmt.KV_SEPARATOR);\n" +
                                        "            sb.append(getf" + zFieldName + "()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);\n";
                                    }
                                }
                                zFileContent += "\n    public " + zClassName + "(Map<String, String> inputMsgFields) {\n" +
                                "        super(inputMsgFields);\n" +
                                "    }\n" +
                                "    \n" + "    //.getsets:" + zFxGetSet + "\n" +
                                "    \n" +
                                "    //.process:\n" +
                                "    public boolean assignMessage(){\n" +
                                "        boolean bOut = false;\n" +
                                "        try{\n" +
                                "            //.assign header:\n" +
                                "            assignHeaderMessage();\n" +
                                "            //.assign data:\n" +
                                "            Map<String, String> mapFields = getMapMsgFields();\n" +
                                "            if ((mapFields != null) && (!mapFields.isEmpty())){\n" +
                                "                for (String zKey : mapFields.keySet()){\n" +
                                "                    String zValue = mapFields.get(zKey);\n" +
                                "                    switch(zKey){\n" +
                                zFxAssign +
                                "                        default:\n" +
                                "                            break;\n" +
                                "                    }\n" +
                                "                }\n" +
                                "                bOut = true;\n" +
                                "            }\n" +
                                "        }catch(Exception ex0){\n" +
                                "            //.EXXX.\n" +
                                "        }\n" +
                                "        return bOut;\n" +
                                "    }\n" +
                                "    \n" +
                                "    public String msgDataToString() {\n" +
                                "        String zOut = \"\";\n" +
                                "        try{\n" +
                                "            StringBuilder sb = new StringBuilder();\n" +
                                zFxMsgToStr +
                                "            zOut = sb.toString();\n" +
                                "        }catch(Exception ex0){\n" +
                                "            //.EXXX.\n" +
                                "        }\n" +
                                "        return zOut;\n" +
                                "    }\n" +
                                "    \n" +
                                "    public String msgToString() {\n" +
                                "        String zOut = \"\";\n" +
                                "        try{\n" +
                                "            StringBuilder sb = new StringBuilder();\n" +
                                "            if (StringHelper.isNullOrEmpty(getfMsgType())){\n" +
                                "                \n" +
                                "            }\n" +
                                "            \n" +
                                "            sb.append(msgHeaderToString());\n" +
                                "            sb.append(msgDataToString());\n" +
                                "            sb.append(msgTrailerToString());\n" +
                                "            \n" +
                                "            zOut = sb.toString();\n" +
                                "        }catch(Exception ex0){\n" +
                                "            //.EXXX.\n" +
                                "        }\n" +
                                "        return zOut;\n" +
                                "    }\n" +
                                "    " +
                                "}";
                                Files.write(Paths.get(zOutFilePath), zFileContent.getBytes(), StandardOpenOption.APPEND);
                            }
                        }
                    }
                }
            }
        }catch(IOException | ParserConfigurationException | SAXException ex0){
            System.out.println(ex0.getMessage());
        }
        return mOut;
    }
}
