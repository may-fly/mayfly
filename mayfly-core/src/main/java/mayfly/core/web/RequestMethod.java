package mayfly.core.web;


import mayfly.core.util.StringUtils;

/**
 * @Description: 请求方法枚举
 * @author: meilin.huang
 * @date: 2018/6/14 上午8:54
 */
public enum RequestMethod {
    /**
     * get请求
     */
    GET((byte)1),
    POST((byte)2), PUT((byte)3), DELETE((byte)4);

    private byte type;

    public static RequestMethod getByMethodName(String requestMethod) {
        RequestMethod method = GET;
        if (StringUtils.isEmpty(requestMethod)) {
            return method;
        }
        switch (requestMethod.toUpperCase()) {
            case "GET" :
                method = GET;
                break;
            case "POST" :
                method = POST;
                break;
            case "PUT" :
                method = PUT;
                break;
            case "DELETE" :
                method = DELETE;
                break;
        }
        return method;
    }

    public static RequestMethod getByType(Byte type) {
        RequestMethod method = GET;
        switch (type) {
            case 1 :
                method = GET;
                break;
            case 2 :
                method = POST;
                break;
            case 3 :
                method = PUT;
                break;
            case 4 :
                method = DELETE;
                break;
        }
        return method;
    }

    RequestMethod(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }
}
