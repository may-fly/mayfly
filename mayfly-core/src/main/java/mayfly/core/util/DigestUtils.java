package mayfly.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:20
 */
public class DigestUtils {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    /**
     * md5加密
     *
     * @param data 需要加密的字符串
     * @return 加密后字符串
     */
    public static String md5DigestAsHex(String data) {
        return digestAsHex(data, "md5");
    }


    public static String digestAsHex(String data, String algorithm) {
        return new String(encodeHex(getDigest(algorithm).digest(data.getBytes(StandardCharsets.UTF_8))));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = HEX_CHARS[(0xF0 & data[i]) >>> 4];
            out[j++] = HEX_CHARS[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 获取消息摘要
     *
     * @param algorithm 算法摘要
     * @return message digest
     */
    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("无法获取消息摘要：" + algorithm + "\"", ex);
        }
    }
}
