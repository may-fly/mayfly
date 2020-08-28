package mayfly.sys.module.machine.service.impl;

import mayfly.sys.module.machine.service.MachineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MachineServiceImplTest {

    @Autowired
    private MachineServiceImpl machineService;

    @Test
    public void runShell() {
        String monitor = machineService.runShell(1L, "monitor");
        System.out.println(monitor);
    }

    @Test
    public void getTopInfo() {
        MachineServiceImpl.TopInfo topInfo = machineService.getTopInfo(1L);
        System.out.println(topInfo);
    }
}