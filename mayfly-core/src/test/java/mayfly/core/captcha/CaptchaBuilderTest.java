package mayfly.core.captcha;

import org.junit.Test;

public class CaptchaBuilderTest {


    @Test
    public void testBuilder() {
        ArithmeticCaptcha ac = CaptchaBuilder.<ArithmeticCaptcha>newArithmeticBuilder().len(3).build();
        String text = ac.text();
        String arithmeticString = ac.getArithmeticString();
        System.out.println(arithmeticString + " " + text);
    }
}