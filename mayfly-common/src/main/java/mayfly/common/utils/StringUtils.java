package mayfly.common.utils;

/**
 * @Description: String工具类
 * @author: hml
 * @date: 2018/6/14 下午3:01
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 驼峰模式字符串转换为下划线字符串
     * @param camelStr
     * @return
     */
    public static String camel2Underscore(String camelStr) {
        return convertCamel(camelStr, '_');
    }

    /**
     * 转换驼峰字符串为指定分隔符的字符串 <br/>
     * 如：camelStr:"UserInfo"    separator:'_' <br/>
     * return "user_info"
     * @param camelStr  驼峰字符串
     * @param separator  分隔符
     * @return
     */
    public static String convertCamel(String camelStr, char separator) {
        if (isEmpty(camelStr)) {
            return camelStr;
        }
        StringBuilder out = new StringBuilder();
        char[] strChar = camelStr.toCharArray();
        for (int i = 0; i < strChar.length; i++) {
            char c = strChar[i];
            if (!Character.isLowerCase(c)) {
                //如果是首字符，则不需要添加分隔符
                if (i == 0) {
                    out.append(Character.toLowerCase(c));
                    continue;
                }
                out.append(separator).append(Character.toLowerCase(c));
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

//    public static void main(String[] argsa) {
//        String str = "UserInfoDtoServiceAction";
//        System.out.println(camel2Underscore(str));
//    }
}
