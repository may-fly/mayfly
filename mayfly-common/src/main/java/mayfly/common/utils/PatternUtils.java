package mayfly.common.utils;

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
        private Map<String, String> store = new HashMap<>();

        public void put(String key, String value) {
            store.put(key, value);
        }

        public String get(String key) {
            return store.get(key);
        }
    }

    public static void main(String[] args) throws Exception{
        String urlPattern = "/v1/hospital/{id}/doctors/1";
        String uri = "/v1/hospital/1/doctors/1";
        String uri2 = "/v1/hospital/doctors";

//        PatternUtils p = new PatternUtils();
//        p.set("1", "haha");
//
//        PatternUtils p2 = new PatternUtils();
//        p2.set("1","hehe");
//        System.out.println(p.get("1"));
//        System.out.println(p2.get("1"));
//
//        BitSet bit = new BitSet(21);
//        System.out.println(Integer.toBinaryString(65));
//        System.out.println(128 >> 6);

        System.out.println(match(uri, Arrays.asList("/v1/hospital/{id}/doctors/{did}", "/v1/hospital/doctors")));
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
