package mayfly.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author meilin.huang
 * @date 2020-07-17 10:12 上午
 */
public class FileUtils {

    /**
     * xml文件后缀名
     */
    public static final String XML_FILE_EXTENSION = ".xml";

    /**
     * 获取文件输入流
     *
     * @param file file
     * @return stream
     */
    public static FileInputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据文件全名获取文件类型，如 test.jpg 返回.jpg
     *
     * @param completeFileName 文件全名
     * @return 文件后缀名即类型
     */
    public static String getFileType(String completeFileName) {
        int lastIndexOf = completeFileName.lastIndexOf(".");
        return lastIndexOf == -1 ? "" : completeFileName.substring(lastIndexOf);
    }

    /**
     * 根据文件全名获取文件类型，如 test.jpg 返回test
     *
     * @param completeFileName 文件全名
     * @return 文件名，不包含后缀类型
     */
    public static String getFileName(String completeFileName) {
        int lastIndexOf = completeFileName.lastIndexOf(".");
        return lastIndexOf == -1 ? completeFileName : completeFileName.substring(0, lastIndexOf);
    }

    /**
     * 根据文件全路径获取获取文件名，如 /usr/local/test.jpg 返回test.jpg
     *
     * @param filepath 文件路径名
     * @return 文件名，不包含后缀类型
     */
    public static String getFileNameByPath(String filepath) {
        int lastIndexOf = filepath.lastIndexOf("/");
        return lastIndexOf == -1 ? filepath : filepath.substring(lastIndexOf + 1);
    }

    /**
     * 解压<br>
     *
     * @param inputStream zip文件流，包含编码信息，默认utf8编码
     * @return 解压的目录
     */
    public static List<ZipFileContent> unzip(InputStream inputStream) {
        return unzip(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 解压<br>
     *
     * @param inputStream zip文件流，包含编码信息
     * @param charset     字符编码
     * @return 解压的目录
     */
    public static List<ZipFileContent> unzip(InputStream inputStream, Charset charset) {
        return unzip(new ZipInputStream(inputStream, charset));
    }

    /**
     * 解压<br>
     *
     * @param zipStream zip文件流，包含编码信息
     * @return 解压的目录
     */
    public static List<ZipFileContent> unzip(ZipInputStream zipStream) {
        try {
            List<ZipFileContent> zips = new ArrayList<>();
            ZipEntry zipEntry;
            while ((zipEntry = zipStream.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    zips.add(new ZipFileContent(zipEntry.getName(), zipStream));
                }
            }
            return zips;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.close(zipStream);
        }
    }


    /**
     * zip文件里的文件内容
     */
    public static class ZipFileContent {
        private final String fileName;

        private final byte[] contents;

        ZipFileContent(String fileName, ZipInputStream in) {
            this.fileName = fileName;
            try {
                contents = IOUtils.readByte(in, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContents() {
            return contents;
        }
    }

}
