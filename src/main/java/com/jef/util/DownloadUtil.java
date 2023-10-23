package com.jef.util;

import org.apache.commons.net.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Jef
 * @date 2021/7/9
 */
public class DownloadUtil {

    /**
     * url内容转base64
     * @author Jef
     * @date 2021/7/9
     * @param url
     * @return java.lang.String
     */
    public static String getUrlTransferBase64(String url) throws IOException {
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) getOutPutStream(url, null);
        String base64 = new String(Base64.encodeBase64(outputStream.toByteArray()), "UTF-8");
        return base64;
    }

    /**
     * 获取输出流
     * @author Jef
     * @date 2021/7/9
     * @param fileUrl
     * @param outputStream
     * @return java.io.OutputStream
     */
    public static OutputStream getOutPutStream(String fileUrl, OutputStream outputStream) {
        URL url;
        HttpURLConnection conn = null;
        if (outputStream == null) {
            outputStream = new ByteArrayOutputStream();
        }
        BufferedInputStream inputStream = null;
        try {
            url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(20 * 1000);
            inputStream = new BufferedInputStream(conn.getInputStream());
            byte[] buf = new byte[1024];
            int size = 0;
            // 最大值2147483647  约2Gb
            int fileLength = conn.getContentLength();
            int downloadLength = 0;
            while ((size = inputStream.read(buf)) != -1) {
                downloadLength += size;
                outputStream.write(buf, 0, size);
                // 防止最后一次读取的时候，一直阻塞
                if(fileLength == downloadLength){
                    break;
                }
            }
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 下载文件
     * @param fileUrl 文件地址
     * @param folderPath 保存路径
     * @return
     */
    public static String download(String fileUrl, String folderPath) {
        String subPrefix = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        String downloadSaveUrl = folderPath + File.separator + subPrefix;
        File file = new File(downloadSaveUrl);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            getOutPutStream(fileUrl, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return downloadSaveUrl;
    }

}