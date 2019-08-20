package mayfly.common.result;

import com.alibaba.fastjson.JSON;
import mayfly.common.enums.NameValueEnum;
import mayfly.common.enums.ValueEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一结果返回
 * @author meilin.huang
 * @version 1.0
 * @date 2018-09-14 上午10:53
 */
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = 6992257459533918156L;

    /**
     * 操作结果码
     */
    final private Integer code;

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
     * 可扩展结果操作码和操作结果消息(即实现{@link NameValueEnum}接口的枚举类即可)
     * @param resultEnum  结果枚举类
     * @return  结果对象
     */
    public static <T, E extends Enum & NameValueEnum> Result<T> of(E resultEnum) {
        return new Result<T>(resultEnum.getValue(), resultEnum.getName());
    }

    /**
     * 操作结果对象简单工厂 <br/>
     * 可扩展结果操作码(即实现{@link ValueEnum}接口的枚举类即可)
     * @param resultEnum  结果枚举类
     * @param msg 覆盖结果对象消息
     * @return  结果对象
     */
    public static <T, E extends Enum & ValueEnum> Result<T> of(E resultEnum, String msg) {
        return new Result<T>(resultEnum.getValue(), msg);
    }

    public static <T, E extends Enum & ValueEnum> Result<T> of(E resultEnum, String msg, T data) {
        return new Result<T>(resultEnum.getValue(), msg, data);
    }


    //---------------------------------------------------------------------
    // 各种结果对象的简单工厂，可使用Result.<T>success()调用返回指定泛型data值的对象(防止部分编译警告)
    //---------------------------------------------------------------------

    public static <T> Result<T> success() {
        return of(ResultEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>success().with(data);
    }

    public static <T> Result<T> error() {
        return of(ResultEnum.ERROR);
    }

    public static <T> Result<T> error(String msg) {
        return of(ResultEnum.ERROR, msg);
    }

    public static <T> Result<T> paramError() {
        return of(ResultEnum.PARAM_ERROR);
    }

    public static <T> Result<T> paramError(String msg) {
        return of(ResultEnum.PARAM_ERROR, msg);
    }

    public static <T> Result<T> serverError() {
        return of(ResultEnum.SERVER_ERROR);
    }

    public static <T> Result<T> serverError(String msg) {
        return of(ResultEnum.SERVER_ERROR, msg);
    }

    public static <T> Result<T> noFound() {
        return of(ResultEnum.NO_FOUND);
    }

    public static <T> Result<T> noFound(String msg) {
        return of(ResultEnum.NO_FOUND, msg);
    }

    public static <T> Result<T> withoutPermission() {
        return of(ResultEnum.NO_PERMISSION);
    }

    /**
     * 将数据对象添加进操作结果{@link Result}对象里
     * @param data  数据对象
     * @return  Result对象
     */
    public Result<T> with(T data) {
        this.data = data;
        return this;
    }

    /**
     * 修改结果msg
     * @param msg  msg
     * @return     Result
     */
    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 判断该结果是否为成功的操作
     * @return true: success
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, ResultEnum.SUCCESS.getValue());
    }



    //---------------------------------------------------------------------
    // getter toString
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
