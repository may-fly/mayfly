package mayfly.core.util;

/**
 * String工具类
 *
 * @author hml
 * @date 2018/6/14 下午3:01
 */
public class StringUtils {

    /**
     * 判断字符串是否为空且长度为0
     *
     * @param str  字符串
     * @return     为空或0返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 驼峰模式字符串转换为下划线字符串
     *
     * @param camelStr  驼峰字符串
     * @return          下划线字符串
     */
    public static String camel2Underscore(String camelStr) {
        return convertCamel(camelStr, '_');
    }

    /**
     * 转换驼峰字符串为指定分隔符的字符串 <br/>
     * 如：camelStr:"UserInfo"    separator:'_' <br/>
     * return "user_info"
     *
     * @param camelStr  驼峰字符串
     * @param separator 分隔符
     * @return          将驼峰字符串转换后的字符串
     */
    public static String convertCamel(String camelStr, char separator) {
        if (isEmpty(camelStr)) {
            return camelStr;
        }
        StringBuilder out = new StringBuilder();
        char[] strChar = camelStr.toCharArray();
        for (int i = 0, len = strChar.length; i < len; i++) {
            char c = strChar[i];
            if (Character.isUpperCase(c)) {
                //如果不是首字符，则需要添加分隔符
                if (i != 0) {
                    out.append(separator);
                }
                out.append(Character.toLowerCase(c));
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }
}
