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
public final class Result implements Serializable {

    private static final long serialVersionUID = 6992257459533918156L;

    /**
     * 操作结果码
     */
    final private Integer code;

    /**
     * 操作结果消息
     */
    final private String msg;

    /**
     * 操作结果数据对象
     */
    private Object data;


    private Result(NameValueEnum resultEnum) {
        this.code = resultEnum.getValue();
        this.msg = resultEnum.getName();
    }

    private Result(ValueEnum resultEnum, String msg) {
        this.code = resultEnum.getValue();
        this.msg = msg;
    }

    /**
     * 操作结果对象简单工厂 <br/>
     * 可扩展结果操作码和操作结果消息(即实现{@link NameValueEnum}接口的枚举类即可)
     * @param resultEnum  结果枚举类
     * @return  结果对象
     */
    public static <T extends Enum & NameValueEnum> Result of(T resultEnum) {
        return new Result(resultEnum);
    }

    /**
     * 操作结果对象简单工厂 <br/>
     * 可扩展结果操作码(即实现{@link ValueEnum}接口的枚举类即可)
     * @param resultEnum  结果枚举类
     * @param msg 覆盖结果对象消息
     * @return  结果对象
     */
    public static <T extends Enum & ValueEnum> Result of(T resultEnum, String msg) {
        return new Result(resultEnum, msg);
    }


    //---------------------------------------------------------------------
    // 各种结果对象的简单工厂
    //---------------------------------------------------------------------

    public static Result error() {
        return of(ResultEnum.ERROR);
    }

    public static Result error(String msg) {
        return of(ResultEnum.ERROR, msg);
    }

    public static Result success(String msg) {
        return of(ResultEnum.SUCCESS, msg);
    }

    public static Result success() {
        return of(ResultEnum.SUCCESS);
    }

    public static Result paramError() {
        return of(ResultEnum.PARAM_ERROR);
    }

    public static Result paramError(String msg) {
        return of(ResultEnum.PARAM_ERROR, msg);
    }

    public static Result serverError() {
        return of(ResultEnum.SERVER_ERROR);
    }

    public static Result serverError(String msg) {
        return of(ResultEnum.SERVER_ERROR, msg);
    }

    public static Result noFound() {
        return of(ResultEnum.NO_FOUND);
    }

    public static Result noFound(String msg) {
        return of(ResultEnum.NO_FOUND, msg);
    }

    public static Result withoutPermission() {
        return of(ResultEnum.NO_PERMISSION);
    }

    /**
     * 操作结果附上数据对象
     * @param data  数据对象
     * @return  Result对象
     */
    public Result withData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 判断该结果是否为成功的操作
     * @return true: success
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, ResultEnum.SUCCESS.getValue());
    }



    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
