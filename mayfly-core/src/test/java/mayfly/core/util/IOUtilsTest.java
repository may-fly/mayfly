package mayfly.core.util;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void readByte() throws Exception {
        byte[] bytes = IOUtils.readByte(IOUtils.class.getResourceAsStream("Assert.class"), true);
        System.out.println(bytes.length);
    }
}