package com.jef.util;

import com.jef.constant.PrintConstants;
import com.jef.exception.IllegalPathException;
import com.jef.filter.IncFileFilter;
import com.jef.io.blog.FileGlobal;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jef.util.StringUtils.trimToEmpty;
import static com.jef.util.StringUtils.trimToNull;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final Pattern schemePrefixPattern = Pattern.compile("(file:*[a-z]:)|(\\w+://.+?/)|((jar|zip):.+!/)|(\\w+:)", Pattern.CASE_INSENSITIVE);

    /**
     * 从指定文件，指定编码读取文件
     *
     * @param file     待读入的文件
     * @param encoding 读入编码
     */
    public static String readFileContent(File file, String encoding) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return readStreamContent(fis, encoding);
    }

    /**
     * 从指定输入流，指定编码读取文件
     *
     * @param stream   待读入的输入流
     * @param encoding 读入编码
     */
    public static String readStreamContent(InputStream stream, String encoding) throws Exception {
        StringBuilder content = new StringBuilder("");
        byte[] bytearray = new byte[stream.available()];
        int bytetotal = stream.available();
        while (stream.read(bytearray, 0, bytetotal) != -1) {
            String temp = new String(bytearray, 0, bytetotal, encoding);
            content.append(temp);
        }
        return content.toString();
    }

    /**
     * 输出代码到指定目录
     *
     * @param file          要输出的文件
     * @param outputContext 输出内容
     */
    public static void writeFileContent(File file, String outputContext) throws IOException {
        boolean b = !file.exists() ? file.createNewFile() : file.delete();

        FileOutputStream fop = new FileOutputStream(file);
        fop.write(outputContext.getBytes());
        fop.flush();
        fop.close();
    }

    /**
     * 规格化绝对路径。
     * <p>
     * 该方法返回以“<code>/</code>”开始的绝对路径。转换规则如下：
     * </p>
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径）。</li>
     * <li>对于绝对路径，如果".."上朔的路径超过了根目录，则看作非法路径，抛出异常。</li>
     * </ol>
     * <p>
     * param path 要规格化的路径
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizeAbsolutePath(String path) throws IllegalPathException {
        return normalizePath(path, true, false, false);
    }

    /**
     * 规格化绝对路径。
     * <p>
     * 该方法返回以“<code>/</code>”开始的绝对路径。转换规则如下：
     * </p>
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径和强制指定<code>removeTrailingSlash==true</code>）。</li>
     * <li>对于绝对路径，如果".."上朔的路径超过了根目录，则看作非法路径，抛出异常。</li>
     * </ol>
     * <p>
     * param path                要规格化的路径
     * param removeTrailingSlash 是否强制移除末尾的<code>"/"</code>
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizeAbsolutePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, true, false, removeTrailingSlash);
    }

    /**
     * 规格化相对路径。
     * <p>
     * 该方法返回不以“<code>/</code>”开始的相对路径。转换规则如下：
     * </p>
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>空相对路径返回""。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径）。</li>
     * </ol>
     * <p>
     * param path 要规格化的路径
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizeRelativePath(String path) throws IllegalPathException {
        return normalizePath(path, false, true, false);
    }

    /**
     * 规格化相对路径。
     * <p>
     * 该方法返回不以“<code>/</code>”开始的相对路径。转换规则如下：
     * </p>
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>空相对路径返回""。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径和强制指定<code>removeTrailingSlash==true</code>）。</li>
     * </ol>
     * <p>
     * param path                要规格化的路径
     * param removeTrailingSlash 是否强制移除末尾的<code>"/"</code>
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizeRelativePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, false, true, removeTrailingSlash);
    }

    /**
     * 规格化路径。规则如下：
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>空绝对路径返回"/"，空相对路径返回""。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径）。</li>
     * <li>对于绝对路径，如果".."上朔的路径超过了根目录，则看作非法路径，抛出异常。</li>
     * </ol>
     * <p>
     * param path 要规格化的路径
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizePath(String path) throws IllegalPathException {
        return normalizePath(path, false, false, false);
    }

    /**
     * 规格化路径。规则如下：
     * <ol>
     * <li>路径为空，则返回<code>""</code>。</li>
     * <li>将所有backslash("\\")转化成slash("/")。</li>
     * <li>去除重复的"/"或"\\"。</li>
     * <li>去除"."，如果发现".."，则向上朔一级目录。</li>
     * <li>空绝对路径返回"/"，空相对路径返回""。</li>
     * <li>保留路径末尾的"/"（如果有的话，除了空路径和强制指定<code>removeTrailingSlash==true</code>）。</li>
     * <li>对于绝对路径，如果".."上朔的路径超过了根目录，则看作非法路径，抛出异常。</li>
     * </ol>
     * <p>
     * param path                要规格化的路径
     * param removeTrailingSlash 是否强制移除末尾的<code>"/"</code>
     * return 规格化后的路径
     * throws IllegalPathException 如果路径非法
     */
    public static String normalizePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, false, false, removeTrailingSlash);
    }

    private static String normalizePath(String path, boolean forceAbsolute, boolean forceRelative, boolean removeTrailingSlash) throws IllegalPathException {
        char[] pathChars = trimToEmpty(path).toCharArray();
        int length = pathChars.length;

        // 检查绝对路径，以及path尾部的"/"
        boolean startsWithSlash = false;
        boolean endsWithSlash = false;

        if (length > 0) {
            char firstChar = pathChars[0];
            char lastChar = pathChars[length - 1];

            startsWithSlash = firstChar == '/' || firstChar == '\\';
            endsWithSlash = lastChar == '/' || lastChar == '\\';
        }

        StringBuilder buf = new StringBuilder(length);
        boolean isAbsolutePath = forceAbsolute || !forceRelative && startsWithSlash;
        int index = startsWithSlash ? 0 : -1;
        int level = 0;

        if (isAbsolutePath) {
            buf.append("/");
        }

        while (index < length) {
            // 跳到第一个非slash字符，或末尾
            index = indexOfSlash(pathChars, index + 1, false);

            if (index == length) {
                break;
            }

            // 取得下一个slash index，或末尾
            int nextSlashIndex = indexOfSlash(pathChars, index, true);

            String element = new String(pathChars, index, nextSlashIndex - index);
            index = nextSlashIndex;

            // 忽略"."
            if (".".equals(element)) {
                continue;
            }

            // 回朔".."
            if ("..".equals(element)) {
                if (level == 0) {
                    // 如果是绝对路径，../试图越过最上层目录，这是不可能的，
                    // 抛出路径非法的异常。
                    if (isAbsolutePath) {
                        throw new IllegalPathException(path);
                    } else {
                        buf.append("../");
                    }
                } else {
                    buf.setLength(pathChars[--level]);
                }

                continue;
            }

            // 添加到path
            pathChars[level++] = (char) buf.length(); // 将已经读过的chars空间用于记录指定level的index
            buf.append(element).append('/');
        }

        // 除去最后的"/"
        if (buf.length() > 0) {
            if (!endsWithSlash || removeTrailingSlash) {
                buf.setLength(buf.length() - 1);
            }
        }

        return buf.toString();
    }

    private static int indexOfSlash(char[] chars, int beginIndex, boolean slash) {
        int i = beginIndex;

        for (; i < chars.length; i++) {
            char ch = chars[i];

            if (slash) {
                if (ch == '/' || ch == '\\') {
                    break; // if a slash
                }
            } else {
                if (ch != '/' && ch != '\\') {
                    break; // if not a slash
                }
            }
        }

        return i;
    }

    // ==========================================================================
    // 取得基于指定basedir规格化路径。
    // ==========================================================================

    /**
     * 如果指定路径已经是绝对路径，则规格化后直接返回之，否则取得基于指定basedir的规格化路径。
     * <p>
     * param basedir 根目录，如果<code>path</code>为相对路径，表示基于此目录
     * param path    要检查的路径
     * return 规格化的绝对路径
     * throws IllegalPathException 如果路径非法
     */
    public static String getAbsolutePathBasedOn(String basedir, String path) throws IllegalPathException {
        // 如果path为绝对路径，则规格化后返回
        boolean isAbsolutePath = false;

        path = trimToEmpty(path);

        if (path.length() > 0) {
            char firstChar = path.charAt(0);
            isAbsolutePath = firstChar == '/' || firstChar == '\\';
        }

        if (!isAbsolutePath) {
            // 如果path为相对路径，将它和basedir合并。
            if (path.length() > 0) {
                path = trimToEmpty(basedir) + "/" + path;
            } else {
                path = trimToEmpty(basedir);
            }
        }

        return normalizeAbsolutePath(path);
    }

    /**
     * 取得和系统相关的绝对路径。
     * <p>
     * throws IllegalPathException 如果basedir不是绝对路径
     */
    public static String getSystemDependentAbsolutePathBasedOn(String basedir, String path) {
        path = trimToEmpty(path);

        boolean endsWithSlash = path.endsWith("/") || path.endsWith("\\");

        File pathFile = new File(path);

        if (pathFile.isAbsolute()) {
            // 如果path已经是绝对路径了，则直接返回之。
            path = pathFile.getAbsolutePath();
        } else {
            // 否则以basedir为基本路径。
            // 下面确保basedir本身为绝对路径。
            basedir = trimToEmpty(basedir);

            File baseFile = new File(basedir);

            if (baseFile.isAbsolute()) {
                path = new File(baseFile, path).getAbsolutePath();
            } else {
                throw new IllegalPathException("Basedir is not absolute path: " + basedir);
            }
        }

        if (endsWithSlash) {
            path = path + '/';
        }

        return normalizePath(path);
    }

    // ==========================================================================
    // 取得相对于指定basedir相对路径。
    // ==========================================================================

    /**
     * 取得相对于指定根目录的相对路径。
     * <p>
     * param basedir 根目录
     * param path    要计算的路径
     * return 如果<code>path</code>和<code>basedir</code>是兼容的，则返回相对于
     * <code>basedir</code>的相对路径，否则返回<code>path</code>本身。
     * throws IllegalPathException 如果路径非法
     */
    public static String getRelativePath(String basedir, String path) throws IllegalPathException {
        // 取得规格化的basedir，确保其为绝对路径
        basedir = normalizeAbsolutePath(basedir);

        // 取得规格化的path
        path = getAbsolutePathBasedOn(basedir, path);

        // 保留path尾部的"/"
        boolean endsWithSlash = path.endsWith("/");

        // 按"/"分隔basedir和path
        String[] baseParts = StringUtils.split(basedir, '/');
        String[] parts = StringUtils.split(path, '/');
        StringBuilder buf = new StringBuilder();
        int i = 0;

        while (i < baseParts.length && i < parts.length && baseParts[i].equals(parts[i])) {
            i++;
        }

        if (i < baseParts.length && i < parts.length) {
            for (int j = i; j < baseParts.length; j++) {
                buf.append("..").append('/');
            }
        }

        for (; i < parts.length; i++) {
            buf.append(parts[i]);

            if (i < parts.length - 1) {
                buf.append('/');
            }
        }

        if (endsWithSlash && buf.length() > 0 && buf.charAt(buf.length() - 1) != '/') {
            buf.append('/');
        }

        return buf.toString();
    }

    // ==========================================================================
    // 取得文件名后缀。
    // ==========================================================================

    /**
     * 取得文件路径的后缀。
     * <ul>
     * <li>未指定文件名 - 返回<code>null</code>。</li>
     * <li>文件名没有后缀 - 返回<code>null</code>。</li>
     * </ul>
     */
    public static String getExtension(String fileName) {
        return getExtension(fileName, null, false);
    }

    /**
     * 取得文件路径的后缀。
     * <ul>
     * <li>未指定文件名 - 返回<code>null</code>。</li>
     * <li>文件名没有后缀 - 返回<code>null</code>。</li>
     * </ul>
     */
    public static String getExtension(String fileName, boolean toLowerCase) {
        return getExtension(fileName, null, toLowerCase);
    }

    /**
     * 取得文件路径的后缀。
     * <ul>
     * <li>未指定文件名 - 返回<code>null</code>。</li>
     * <li>文件名没有后缀 - 返回指定字符串<code>nullExt</code>。</li>
     * </ul>
     */
    public static String getExtension(String fileName, String nullExt) {
        return getExtension(fileName, nullExt, false);
    }

    /**
     * 取得文件路径的后缀。
     * <ul>
     * <li>未指定文件名 - 返回<code>null</code>。</li>
     * <li>文件名没有后缀 - 返回指定字符串<code>nullExt</code>。</li>
     * </ul>
     */
    public static String getExtension(String fileName, String nullExt, boolean toLowerCase) {
        fileName = trimToNull(fileName);

        if (fileName == null) {
            return null;
        }

        fileName = fileName.replace('\\', '/');
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        int index = fileName.lastIndexOf(".");
        String ext = null;

        if (index >= 0) {
            ext = trimToNull(fileName.substring(index + 1));
        }

        if (ext == null) {
            return nullExt;
        } else {
            return toLowerCase ? ext.toLowerCase() : ext;
        }
    }

    /**
     * 取得指定路径的名称和后缀。
     * <p>
     * param path 路径
     * return 路径和后缀
     */
    public static FileNameAndExtension getFileNameAndExtension(String path) {
        return getFileNameAndExtension(path, false);
    }

    /**
     * 取得指定路径的名称和后缀。
     * <p>
     * param path 路径
     * return 路径和后缀
     */
    public static FileNameAndExtension getFileNameAndExtension(String path, boolean extensionToLowerCase) {
        path = StringUtils.trimToEmpty(path);

        String fileName = path;
        String extension = null;

        if (!StringUtils.isEmpty(path)) {
            // 如果找到后缀，则index >= 0，且extension != null（除非name以.结尾）
            int index = path.lastIndexOf('.');

            if (index >= 0) {
                extension = StringUtils.trimToNull(StringUtils.substring(path, index + 1));

                if (!StringUtils.containsNone(extension, "/\\")) {
                    extension = null;
                    index = -1;
                }
            }

            if (index >= 0) {
                fileName = StringUtils.substring(path, 0, index);
            }
        }

        return new FileNameAndExtension(fileName, extension, extensionToLowerCase);
    }

    /**
     * 规格化文件名后缀。
     * <ul>
     * <li>除去两边空白。</li>
     * <li>转成小写。</li>
     * <li>除去开头的“<code>.</code>”。</li>
     * <li>对空白的后缀，返回<code>null</code>。</li>
     * </ul>
     */
    public static String normalizeExtension(String ext) {
        ext = trimToNull(ext);

        if (ext != null) {
            ext = ext.toLowerCase();

            if (ext.startsWith(".")) {
                ext = trimToNull(ext.substring(1));
            }
        }

        return ext;
    }

    /**
     * 根据指定url和相对路径，计算出相对路径所对应的完整url。类似于<code>URI.resolve()</code>
     * 方法，然后后者不能正确处理jar类型的URL。
     */
    public static String resolve(String url, String relativePath) {
        url = trimToEmpty(url);

        Matcher m = schemePrefixPattern.matcher(url);
        int index = 0;

        if (m.find()) {
            index = m.end();

            if (url.charAt(index - 1) == '/') {
                index--;
            }
        }

        return url.substring(0, index) + normalizeAbsolutePath(url.substring(index) + "/../" + relativePath);
    }


    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath 待删除的文件夹路劲
     */
    public static boolean deletefile(String delpath) throws Exception {
        delete(new File(delpath));
        return true;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param file 待删除的文件夹
     */
    public static void delete(File file) throws Exception {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    delete(files[i]);
                }
            }
            file.delete();
        }
    }

    public static class FileNameAndExtension {
        private final String fileName;
        private final String extension;

        private FileNameAndExtension(String fileName, String extension, boolean extensionToLowerCase) {
            this.fileName = fileName;
            this.extension = extensionToLowerCase ? extension.toLowerCase() : extension;
        }

        public String getFileName() {
            return fileName;
        }

        public String getExtension() {
            return extension;
        }


        public String toString() {
            return extension == null ? fileName : fileName + "." + extension;
        }
    }

    /**
     * 发布增量文件时生成项目目录结构
     *
     * @param sourDir    项目发布路径
     * @param destDirPre 输出前缀路径
     */
    public static void createIncDir(String sourDir, String destDirPre) {
        File baseDir = new File(sourDir);
        if (baseDir.isDirectory()) {
            File[] listFiles = baseDir.listFiles(new IncFileFilter());
            if (listFiles != null) {
                for (File file1 : listFiles) {
                    if (new File(destDirPre + File.separator + file1.getPath().substring(file1.getPath().indexOf("WebContent"))).mkdirs()) {
                        createIncDir(file1.getAbsolutePath(), destDirPre);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        File resourceAsFile = Resources.getResourceAsFile(Thread.currentThread().getContextClassLoader(), "fileUtil.txt");
        InputStream resourceAsStream = Resources.getResourceAsStream(Thread.currentThread().getContextClassLoader(), "fileUtil.txt");

        File file = new File(String.valueOf(resourceAsFile));
        System.out.println("1. 从指定地址使用特定编码读取文件：" + readFileContent(file, "UTF-8"));
        System.out.println("2. 从指定输入流使用特定编码读取文件：" + readStreamContent(resourceAsStream, "UTF-8"));
        System.out.println("4. 格式化路径：" + normalizePath(resourceAsFile.getAbsolutePath()) + "," + resourceAsFile.getAbsolutePath());
        System.out.println("3. 获取文件后缀：" + getExtension(resourceAsFile.getName()));

        writeFileContent(resourceAsFile, "abc");
    }

    /**
     * 下载远程文件并保存到本地
     * @param remoteFilePath 远程文件路径
     * @param localFilePath 本地文件路径
     */
    public static void downloadFile(String remoteFilePath, String localFilePath) throws Exception{
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File f = new File(localFilePath);
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            httpUrl.disconnect();
        } finally{
            if(bos != null){
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据地址获得数据的输入流
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            logger.error("根据地址获得数据的输入流异常,异常信息={}", e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                logger.error("根据地址获得数据的输入流异常,异常信息={}", e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 把word输入流写入到对应的file输出流中，不考虑异常的捕获，直接抛出
     * 用于先把内容填充进文件，然后上传，最后删除
     * @param doc word内容
     * @throws IOException
     */
    public static void wordToLocalUpload(XWPFDocument doc, boolean localDelete) throws IOException {
        // 文件的后缀名
        String fileType = "docx";
        String saveFileName = REIDIdentifier.randomEOID() + "." + fileType;
        File saveFile = new File(FileGlobal.DOWN_URL, saveFileName);
        FileOutputStream fopts = new FileOutputStream(saveFile);
        doc.write(fopts);
        fopts.close();
        // 开始使用
        System.out.println(saveFile.getName());
        // 使用结束
        if (localDelete && saveFile.isFile()){
            saveFile.getAbsoluteFile().delete();
        }
    }

    /**
     * 替换内容
     * @author Jef
     * @date 2019/6/12
     * @param doc
     * @param printMap
     * @return void
     */
    public static void replaceDocument(XWPFDocument doc, Map<String, Object> printMap)throws Exception {
        List<XWPFHeader> pageHeaders = doc.getHeaderList();
        for (int i = 0; i < pageHeaders.size(); i++) {
            List<XWPFParagraph> headerPara = pageHeaders.get(i).getParagraphs();
            processParagraphs(doc, headerPara, printMap, true);
        }

        List<XWPFFooter> pageFooters = doc.getFooterList();
        for (int i = 0; i < pageFooters.size(); i++) {
            List<XWPFParagraph> footerPara = pageFooters.get(i).getParagraphs();
            processParagraphs(doc, footerPara, printMap, true);
        }
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        processParagraphs(doc, paragraphList, printMap, false);
        // 获取所有表格
        List<XWPFTable> tables = doc.getTables();

        for (XWPFTable table : tables) {
            // 获取表格的行
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                // 获取表格的每个单元格
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (XWPFTableCell cell : tableCells) {
                    // 获取单元格的内容
                    String text = cell.getText().replaceAll("\\{(<[^>]+>\\s?)+", "{").replaceAll("(\\s?<[^>]+>)+\\}", "}");
                    Iterator<Map.Entry<String, Object>> iter = PrintConstants.printMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (text.contains(key)) {
                            if (value instanceof String) {
                                // 文本替换
                                String replaceValue = printMap.get(value) == null ? " — —" : String.valueOf(printMap.get(value));
                                text = text.replace(key, replaceValue);
                            }
                        }
                    }
                    cell.removeParagraph(0);
                    //写入新内容
                    cell.setText(text);
                }
            }
        }
    }

    /**
     * 处理段落
     * @author Jef
     * @date 2019/5/21
     * @param paragraphList 段落
     * @param printInfoMap 实际信息map
     * @param isTrue
     * @return void
     */
    private static void processParagraphs(XWPFDocument doc, List<XWPFParagraph> paragraphList, Map<String, Object> printInfoMap, boolean isTrue) {




        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                // 尝试这种替换会不会格式变形
                if (isTrue) {
                    String text = getResultText(paragraph.getText(), printInfoMap);
                    for (int i = 0; i < runs.size(); i++) {
                        if (i == 0) {
                            runs.get(i).setText(text, 0);
                        } else {
                            runs.get(i).setText("", 0);
                        }
                    }
                } else {
                    for (XWPFRun run : runs) {
                        String text = run.getText(0);
                        if (text != null && !"".equals(text.trim())) {
                            text = text.replaceAll("\\{(<[^>]+>\\s?)+", "{").replaceAll("(\\s?<[^>]+>)+\\}", "}");
                            boolean isSetText = false;
                            Iterator<Map.Entry<String, Object>> iter = PrintConstants.printMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, Object> entry = iter.next();
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                if (text.contains(key)) {
                                    isSetText = true;
                                    // 文本替换
                                    String replaceValue = StringUtils.isEmpty(printInfoMap.get(value)) ? " — —" : String.valueOf(printInfoMap.get(value));
                                    text = text.replace(key, replaceValue);
                                    System.out.println(key + "  替换为： " + replaceValue);
                                    break;
                                }
                            }
                            if (isSetText) {
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取最终文本
     * @author Jef
     * @date 2019/5/21
     * @param
     * @return java.lang.String
     */
    private static String getResultText(String text, Map<String, Object> printInfoMap) {
        if (StringUtils.isNotEmpty(text)) {
            text = text.replaceAll("\\{(<[^>]+>\\s?)+", "{").replaceAll("(\\s?<[^>]+>)+\\}", "}");
            // 一行中有多个需要匹配替换的项，为了避免遍历中的map的数量过大，使用这个方式进行break
            Integer leftBrace = StringUtils.getListFromString(text, "\\{").size() - 1, rightBrace = StringUtils.getListFromString(text, "\\}").size() - 1;
            Integer oneLineNum = leftBrace.compareTo(rightBrace) >= 0 ? leftBrace : rightBrace;
            Integer countNum = 0;
            for (Map.Entry<String, Object> entry : PrintConstants.printMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (text.contains(key)) {
                    // 文本替换
                    text = text.replace(key, StringUtils.isEmpty(printInfoMap.get(value)) ? " — —" : String.valueOf(printInfoMap.get(value)));
                    if ((++countNum).equals(oneLineNum)) {
                        break;
                    }
                }
            }
            return text;
        } else {
            return "";
        }
    }
}