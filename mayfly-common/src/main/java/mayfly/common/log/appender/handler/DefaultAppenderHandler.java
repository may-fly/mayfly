package mayfly.common.log.appender.handler;

import mayfly.common.log.appender.LogVO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-29 5:33 PM
 */
public class DefaultAppenderHandler implements AppenderHandler {
    @Override
    public void handler(LogVO log) {
        System.out.print(log);
    }
}
