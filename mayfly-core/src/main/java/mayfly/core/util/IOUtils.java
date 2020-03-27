package mayfly.core.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-10 8:08 下午
 */
public class IOUtils {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    /**
     * 使用NIO拷贝流
     *
     * @param in  输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IOException io异常
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        return copy(Channels.newChannel(in), Channels.newChannel(out), DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in  输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IOException IO异常
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
        Assert.notNull(in, "FileInputStream must not be null!");
        Assert.notNull(out, "FileOutputStream must not be null!");

        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = in.getChannel();
            outChannel = out.getChannel();
            return inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            close(outChannel, inChannel);
        }
    }

    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in         {@link ReadableByteChannel}
     * @param out        {@link WritableByteChannel}
     * @param bufferSize 缓冲大小，如果小于等于0，使用默认
     * @return 拷贝的字节数
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize) throws IOException {
        Assert.notNull(in, "InputStream is null !");
        Assert.notNull(out, "OutputStream is null !");

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        long size = 0;
        // 如果通道已到达流末尾，则为-1
        while (in.read(byteBuffer) != -1) {
            byteBuffer.flip();
            // 写转读
            size += out.write(byteBuffer);
            byteBuffer.clear();
        }
        return size;
    }

    /**
     * 读取指定长度的byte数组，不关闭流
     *
     * @param in     {@link InputStream} 为null返回null
     * @param length 长度，小于等于0返回空byte数组
     * @return bytes
     */
    public static byte[] readByte(InputStream in, int length, final boolean close) throws IOException {
        if (in == null) {
            return null;
        }
        if (length <= 0) {
            return new byte[0];
        }
        try {
            byte[] b = new byte[length];
            int readLength = in.read(b);
            if (readLength > 0 && readLength < length) {
                byte[] b2 = new byte[readLength];
                System.arraycopy(b, 0, b2, 0, readLength);
                return b2;
            }

            return b;
        } finally {
            if (close) {
                close(in);
            }
        }
    }

    /**
     * 获取流的字节数组
     *
     * @param inputStream 输入流
     * @param close       是否关闭
     * @return 字节数组
     * @throws IOException io异常
     */
    public static byte[] readByte(final InputStream inputStream, final boolean close) throws IOException {
        Assert.notNull(inputStream, "inputStream must not be null");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[DEFAULT_BUFFER_SIZE];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            if (close) {
                close(inputStream);
            }
        }
    }


    /**
     * 从InputStream中读取String
     *
     * @param is      输入流
     * @param charset 字符编码
     * @return String
     */
    public static String read(InputStream is, Charset charset) {
        BufferedReader reader = getReader(is, charset);
        try {
            return read(reader);
        } finally {
            close(reader);
        }
    }

    /**
     * 从InputStream中读取String（默认使用utf8编码）
     *
     * @param is 输入流
     * @return String
     */
    public static String read(InputStream is) {
        return read(is, StandardCharsets.UTF_8);
    }


    /**
     * 从Reader中读取String，读取完毕后并不关闭Reader
     *
     * @param reader Reader
     * @return String
     */
    public static String read(Reader reader) {
        final StringBuilder builder = new StringBuilder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (-1 != reader.read(buffer)) {
                builder.append(buffer.flip().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    /**
     * 获得一个Reader
     *
     * @param in      输入流
     * @param charset 字符集
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, Charset charset) {
        if (in == null) {
            return null;
        }

        InputStreamReader reader;
        if (charset == null) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }


    /**
     * 从流中获取文本内容，并使用{@link LineProcessor}处理每行文本
     *
     * @param is            输入流
     * @param lineProcessor 行处理器
     */
    public static void processReadLine(InputStream is, LineProcessor lineProcessor) {
        BufferedReader reader = getReader(is, null);
        try {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    lineProcessor.process(lineNum++, line);
                } catch (Exception e) {
                    throw new RuntimeException("err line:" + line, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(reader);
        }
    }

    /**
     * 关闭指定资源列表
     *
     * @param closeables 需要关闭的资源列表
     */
    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 行处理器，用于处理字符串流中每行的数据内容
     */
    @FunctionalInterface
    public interface LineProcessor {

        /**
         * 处理行
         *
         * @param lineContent 每行的内容
         * @param lineNum     行号，从1开始
         * @throws Exception 处理过程出现的异常
         */
        void process(int lineNum, String lineContent) throws Exception;
    }
}
