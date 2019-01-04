package mayfly.common.log.appender;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-29 5:15 PM
 */
public class LogVO {

    private String project;

    private String ip;

    private String msg;

    public LogVO(){}
    public LogVO(String project, String ip, String msg) {
        this.project = project;
        this.ip = ip;
        this.msg = msg;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
