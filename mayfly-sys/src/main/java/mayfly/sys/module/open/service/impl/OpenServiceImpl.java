package mayfly.sys.module.open.service.impl;

import mayfly.core.captcha.ArithmeticCaptcha;
import mayfly.core.captcha.CaptchaBuilder;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.UUIDUtils;
import mayfly.sys.common.cache.CacheKey;
import mayfly.sys.module.open.controller.vo.CaptchaVO;
import mayfly.sys.module.open.service.OpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @date 2020-04-29 1:33 下午
 */
@Service
public class OpenServiceImpl implements OpenService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public CaptchaVO generateCaptcha() {
        ArithmeticCaptcha ac = CaptchaBuilder.<ArithmeticCaptcha>newArithmeticBuilder()
                .len(3).build();
        String text = ac.text();
        String uuid = UUIDUtils.generateUUID();
        String key = BracePlaceholder.resolve(CacheKey.CAPTCHA_KEY, uuid);
        redisTemplate.opsForValue().set(key, text, CacheKey.CAPTCHA_EXPIRE_TIME, TimeUnit.MINUTES);
        return new CaptchaVO(uuid, ac.toBase64());
    }

    @Override
    public boolean checkCaptcha(String uuid, String captcha) {
        String key = BracePlaceholder.resolve(CacheKey.CAPTCHA_KEY, uuid);
        String text = (String) redisTemplate.opsForValue().get(key);
        BusinessAssert.notNull(text, "验证码过期");
        if (!captcha.equalsIgnoreCase(text)) {
            return false;
        }
        redisTemplate.delete(key);
        return true;
    }
}
