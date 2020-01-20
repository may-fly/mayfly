package mayfly.core.util;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class JVMUtilsTest {

    @Test
    public void testJstack() {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
            System.out.println(JVMUtils.getThreadDumpString(threadInfo));
        }
    }

}