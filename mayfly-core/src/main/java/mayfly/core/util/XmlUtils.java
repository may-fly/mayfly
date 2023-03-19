package mayfly.core.util;

/**
 * @author meilin.huang
 * @date 2021-09-14 2:36 下午
 */
public class XmlUtils {

    public static String toTag(String xml, String tag) {
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int si = xml.indexOf(startTag);
        int ei = xml.indexOf(endTag);
        if (si == -1 || ei == -1) {
            try {
//                return clazz.newInstance();
                return "";
            } catch (Exception e) {
                return null;
            }
        }
        String res = xml.substring(si, ei + endTag.length());
        return res;
    }

    public static void main(String[] args) {
        String xml = "<BODY><ENTITY a=121><test>4242423</test><test2></test2></ENTITY></BODY>";
        System.out.println(toTag(xml, "ENTITY"));
    }

}
