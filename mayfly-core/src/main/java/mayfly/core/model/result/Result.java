package mayfly.core.model.result;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一结果返回
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-09-14 上午10:53
 */
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = 6992257459533918156L;

    /**
     * 操作结果码
     */
    private Integer code;

    /**
     * 操作结果消息
     */
    private String msg;

    /**
     * 操作结果数据对象
     */
    private T data;

    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 操作结果对象简单工厂 <br/>
     * 可扩展结果操作码和操作结果消息(即实现{@link CodeMessage}接口的枚举类即可)
     *
     * @param resultEnum 结果枚举类
     * @return 结果对象
     */
    public static <T> Result<T> of(CodeMessage resultEnum, Object... params) {
        return new Result<T>(resultEnum.getCode(), resultEnum.getMessage(params));
    }

    //---------------------------------------------------------------------
    // 各种结果对象的简单工厂，可使用Result.<T>success()调用返回指定泛型data值的对象(防止部分编译警告)
    //---------------------------------------------------------------------

    /**
     * 成功结果 （结果枚举为 {@linkplain CommonCodeEnum#SUCCESS}）
     *
     * @param <T> data类型
     * @return result
     */
    public static <T> Result<T> success() {
        return of(CommonCodeEnum.SUCCESS);
    }

    /**
     * 成功结果 （结果枚举为 {@linkplain CommonCodeEnum#SUCCESS}）
     *
     * @param <T> data类型
     * @return result
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>success().with(data);
    }

    /**
     * 错误结果
     *
     * @param errorCode 错误码对象
     * @param <T>       实体类型
     * @return result
     */
    public static <T> Result<T> error(Integer errorCode, String errorMsg) {
        return new Result<T>(errorCode, errorMsg);
    }

    /**
     * 操作失败（通常是业务逻辑错误），使用默认错误信息（错误枚举为 {@linkplain CommonCodeEnum#FAILURE}）
     *
     * @param <T> 结果泛型
     * @return result
     */
    public static <T> Result<T> failure() {
        return of(CommonCodeEnum.FAILURE);
    }

    /**
     * 操作失败，通常是业务逻辑错误（错误枚举为 {@linkplain CommonCodeEnum#FAILURE}）
     *
     * @param msg 失败原因
     * @param <T> 结果泛型
     * @return result
     */
    public static <T> Result<T> failure(String msg) {
        return of(CommonCodeEnum.FAILURE, msg);
    }

    /**
     * 请求参数错误，使用默认错误信息（错误枚举为 {@linkplain CommonCodeEnum#PARAM_ERROR}）
     *
     * @param <T> 结果泛型
     * @return result
     */
    public static <T> Result<T> paramError() {
        return of(CommonCodeEnum.PARAM_ERROR);
    }

    /**
     * 请求参数错误（错误枚举为 {@linkplain CommonCodeEnum#PARAM_ERROR}）
     *
     * @param <T> 结果泛型
     * @param msg 具体参数错误信息
     * @return result
     */
    public static <T> Result<T> paramError(String msg) {
        return of(CommonCodeEnum.PARAM_ERROR, msg);
    }

    /**
     * 服务器异常（未知异常），使用默认错误信息（错误枚举为 {@linkplain CommonCodeEnum#SERVER_ERROR}）
     *
     * @param <T> T
     * @return result
     */
    public static <T> Result<T> serverError() {
        return of(CommonCodeEnum.SERVER_ERROR);
    }

    /**
     * 服务器异常（未知异常）（错误枚举为 {@linkplain CommonCodeEnum#SERVER_ERROR}）
     *
     * @param msg 具体异常信息
     * @param <T> T
     * @return result
     */
    public static <T> Result<T> serverError(String msg) {
        return of(CommonCodeEnum.SERVER_ERROR, msg);
    }

    /**
     * 资源未找到result
     *
     * @param <T> T
     * @return result
     */
    public static <T> Result<T> noFound() {
        return of(CommonCodeEnum.NO_FOUND);
    }

    public static <T> Result<T> noFound(String msg) {
        return of(CommonCodeEnum.NO_FOUND, msg);
    }

    public static <T> Result<T> withoutPermission() {
        return of(CommonCodeEnum.NO_PERMISSION);
    }

    /**
     * 将数据对象添加进操作结果{@link Result}对象里
     *
     * @param data 数据对象
     * @return Result对象
     */
    public Result<T> with(T data) {
        this.data = data;
        return this;
    }

    /**
     * 判断该结果是否为成功的操作
     *
     * @return true: success
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, CommonCodeEnum.SUCCESS.getCode());
    }
    

    //---------------------------------------------------------------------
    // getter setter toString
    //---------------------------------------------------------------------

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
