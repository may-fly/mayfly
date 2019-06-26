package mayfly.common.util;

import java.util.*;

/**
 * @Description: 正则工具
 * @author: hml
 * @date: 2018/6/8 上午9:38
 */
public class PatternUtils {

    private int i;

    public int getI() {
        return this.i;
    }

    PatternUtils.Registery registery = new PatternUtils.Registery();

    public void set(String key, String value) {
        registery.put(key, value);
    }

    public String get(String key) {
        return registery.get(key);
    }

    class Registery{
        private Map<String, String> store = new HashMap();

        public void put(String key, String value) {
            store.put(key, value);
        }

        public String get(String key) {
            return store.get(key);
        }
    }


    public static boolean match(String requestUri, List<String> uris) {
        if (uris.contains(requestUri)) {
            return true;
        }
        for (String uri : uris) {
            if (doMatch(requestUri, uri)) {
                return true;
            }
        }
        return false;
    }

    public static boolean doMatch(String requestUri, String uriPattern) {
        String[] rUri = requestUri.split("/");
        String[] uriP = uriPattern.split("/");
        System.out.println(Arrays.toString(rUri));
        System.out.println(Arrays.toString(uriP));
        if (rUri.length != uriP.length) {
            return false;
        }
        for (int i = 0; i < rUri.length; i++) {
            String r = rUri[i];
            String up = uriP[i];
            if (r.equals(up)) {
                continue;
            }

            if (up.matches("\\{\\w*\\}")) {
                continue;
            }
            return false;
        }
        return true;
    }

}
