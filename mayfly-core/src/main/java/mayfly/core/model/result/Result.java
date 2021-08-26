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
    private String code;

    /**
     * 操作结果消息
     */
    private String msg;

    /**
     * 操作结果数据对象
     */
    private T data;

    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 操作结果对象简单工厂
     *
     * @param code 错误码对象
     * @param <T>  实体类型
     * @return result
     */
    public static <T> Result<T> of(String code, String msg) {
        return new Result<T>(code, msg);
    }

    /**
     * 操作结果对象简单工厂 <br/>
     * 可扩展结果操作码和操作结果消息(即实现{@link CodeMessage}接口的枚举类即可)
     *
     * @param resultEnum 结果枚举类
     * @return 结果对象
     */
    public static <T> Result<T> of(CodeMessage resultEnum, Object... params) {
        return new Result<T>(resultEnum.getCode(), params == null ? resultEnum.getMessage() : resultEnum.getMessage(params));
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

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(String code) {
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
