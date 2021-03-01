package mayfly.core.captcha;

import mayfly.core.captcha.base.BaseCaptcha;
import mayfly.core.exception.BizAssert;

/**
 * 验证码建造器
 *
 * @author meilin.huang
 * @date 2020-04-29 12:03 下午
 */
public class CaptchaBuilder<T extends BaseCaptcha> {

    private final T captcha;

    @SuppressWarnings("unchecked")
    private CaptchaBuilder(BaseCaptcha captcha) {
        this.captcha = (T) captcha;
    }

    /**
     * 返回普通png验证码builder对象
     *
     * @return builder
     */
    public static <T extends BaseCaptcha> CaptchaBuilder<T> newBuilder() {
        return new CaptchaBuilder<T>(new SpecCaptcha());
    }

    /**
     * 返回算术验证码builder对象
     *
     * @return builder
     */
    public static <T extends BaseCaptcha> CaptchaBuilder<T> newArithmeticBuilder() {
        return new CaptchaBuilder<T>(new ArithmeticCaptcha());
    }

    /**
     * 返回gif验证码builder对象
     *
     * @return builder
     */
    public static <T extends BaseCaptcha> CaptchaBuilder<T> newGifBuilder() {
        return new CaptchaBuilder<T>(new GifCaptcha());
    }

    /**
     * 验证码长度
     *
     * @param len 长度
     * @return builder
     */
    public CaptchaBuilder<T> len(int len) {
        captcha.setLen(len);
        return this;
    }

    public CaptchaBuilder<T> font(int font) {
        try {
            captcha.setFont(font);
        } catch (Exception e) {
            throw BizAssert.newException("字体错误");
        }
        return this;
    }

    /**
     * 验证码宽度
     *
     * @param width 宽度
     * @return builder
     */
    public CaptchaBuilder<T> width(int width) {
        captcha.setWidth(width);
        return this;
    }

    /**
     * 验证码高度
     *
     * @param height 高度
     * @return builder
     */
    public CaptchaBuilder<T> height(int height) {
        captcha.setHeight(height);
        return this;
    }

    /**
     * 字符类型
     *
     * @param charType 字符类型
     * @return builder
     */
    public CaptchaBuilder<T> charType(int charType) {
        captcha.setCharType(charType);
        return this;
    }

    public T build() {
        return captcha;
    }
}
