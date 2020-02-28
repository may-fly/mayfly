package mayfly.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void convertCamel() {
        System.out.println(StringUtils.convertCamel("UserInfo", '.'));
    }
}