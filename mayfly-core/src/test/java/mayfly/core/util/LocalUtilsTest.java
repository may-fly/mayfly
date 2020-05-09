package mayfly.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocalUtilsTest {

    @Test
    public void getLocalAddress() {
        System.out.println(LocalUtils.getIp());
    }
}