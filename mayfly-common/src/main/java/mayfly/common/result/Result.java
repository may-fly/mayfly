package mayfly.common.result;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * web 统一结果返回
 * @author meilin.huang
 * @version 1.0
 * @date 2018-09-14 上午10:53
 */
public final class Result implements Serializable {
    /**
     * 操作成功
     */
    private static final int SUCCESS = 200;
    /**
     * 操作失败
     */
    private static final int ERROR = 400;
    /**
     * 参数错误
     */
    private static final int PARAM_ERROR = 405;
    /**
     * 没有找到资源
     */
    private static final int NO_FOUND = 404;
    /**
     * 服务器异常
     */
    private static final int SERVER_ERROR = 500;
    /**
     * 没有权限
     */
    private static final int NO_PERMISSION = 501;

    private Integer code;
    private String msg;
    private Object data;

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result error(String msg) {
        return new Result(ERROR, msg);
    }

    public static Result success(String msg) {
        return new Result(SUCCESS, msg);
    }

    public static Result success() {
        return new Result(SUCCESS, "success");
    }

    public static Result paramError() {
        return new Result(PARAM_ERROR, "参数错误！");
    }

    public static Result paramError(String msg) {
        return new Result(PARAM_ERROR, msg);
    }

    public static Result serverError() {
        return new Result(SERVER_ERROR, "服务器异常！");
    }

    public static Result serverError(String msg) {
        return new Result(SERVER_ERROR, msg);
    }

    public static Result noFound() {
        return new Result(NO_FOUND, "未找到对应的资源");
    }

    public static Result noFound(String msg) {
        return new Result(NO_FOUND, msg);
    }

    public static Result withoutPermission() {
        return new Result(NO_PERMISSION, "没有权限！");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public Result withData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
