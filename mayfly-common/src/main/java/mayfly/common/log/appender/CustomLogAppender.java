package mayfly.common.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.alibaba.fastjson.JSON;
import mayfly.common.utils.DateUtils;
import mayfly.common.utils.LocalUtils;

import java.time.LocalDateTime;

/**
 * 自定义日志输出
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-29 10:25 AM
 */
public class CustomLogAppender extends AppenderBase<ILoggingEvent> {

    private static final String IP = LocalUtils.getLocalAddress();

    /**
     * 日志格式编码
     */
    private Encoder<ILoggingEvent> encoder;

    /**
     * 项目名
     */
    private String project;

//    static {
//        //自定义loggerAppender加入到logback的rootLogger中
//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
//
//    }


    @Override
    protected void append(ILoggingEvent e) {
        if (e.getLevel() == Level.ERROR) {
            doError(e);
        }
        String msg = e.getMessage();
        String date = DateUtils.defaultFormat(LocalDateTime.now());
        LogVO logVO = new LogVO(project, IP, msg);
        System.out.println(JSON.toJSONString(logVO));
    }


    private void doError(ILoggingEvent e) {

    }



    public Encoder<ILoggingEvent> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
