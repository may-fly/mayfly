package mayfly.core.captcha.base;

import java.security.SecureRandom;

/**
 * @author meilin.huang
 * @date 2020-04-29 11:19 上午
 */
public class Randoms {
    protected static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 定义验证码字符.去除了0、O、I、L等容易混淆的字母
      */
    public static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    protected static final int numMaxIndex = 8;
    /**
     * 数字的最大索引，不包括最大值
      */
    protected static final int charMinIndex = numMaxIndex;
    // 字符的最小索引，包括最小值
    protected static final int charMaxIndex = CHARS.length;
    // 字符的最大索引，不包括最大值
    protected static final int upperMinIndex = charMinIndex;
    // 大写字符最小索引
    protected static final int upperMaxIndex = upperMinIndex + 23;
    // 大写字符最大索引
    protected static final int lowerMinIndex = upperMaxIndex;
    // 小写字母最小索引
    protected static final int lowerMaxIndex = charMaxIndex;
    // 小写字母最大索引

    /**
     * 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int num(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 产生0-num的随机数,不包括num
     *
     * @param num 最大值
     * @return 随机数
     */
    public static int num(int num) {
        return RANDOM.nextInt(num);
    }

    /**
     * 返回CHARS中的随机字符
     *
     * @return 随机字符
     */
    public static char getChar() {
        return CHARS[num(CHARS.length)];
    }

    /**
     * 返回CHARS中第0位到第num位的随机字符
     *
     * @param num 到第几位结束
     * @return 随机字符
     */
    public static char getChar(int num) {
        return CHARS[num(num)];
    }

    /**
     * 返回CHARS中第min位到第max位的随机字符
     *
     * @param min 从第几位开始
     * @param max 到第几位结束
     * @return 随机字符
     */
    public static char getChar(int min, int max) {
        return CHARS[num(min, max)];
    }
}
