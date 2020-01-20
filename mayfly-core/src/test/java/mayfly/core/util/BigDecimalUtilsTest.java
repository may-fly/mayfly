package mayfly.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalUtilsTest {

    @Test
    public void between() {
        BigDecimal value = new BigDecimal("2.01");
        BigDecimal value2 = new BigDecimal("2.011");
        BigDecimal min = new BigDecimal("1.01");
        BigDecimal max = new BigDecimal("2.01");
        System.out.println(this.toString());
        System.out.println(this.hashCode());
        Assert.assertTrue(BigDecimalUtils.between(value, min, max));
        Assert.assertFalse(BigDecimalUtils.between(value2, min, max));
    }
}