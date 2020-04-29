package mayfly.sys.module.open.service;

import mayfly.sys.module.open.controller.vo.CaptchaVO;

/**
 * @author meilin.huang
 * @date 2020-04-29 1:30 下午
 */
public interface OpenService {

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    CaptchaVO generateCaptcha();

    /**
     * 校验验证码
     *
     * @param uuid    uuid
     * @param captcha 验证码
     * @return 是否正确
     */
    boolean checkCaptcha(String uuid, String captcha);
}
