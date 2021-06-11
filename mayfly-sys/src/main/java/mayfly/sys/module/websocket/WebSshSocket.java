package mayfly.sys.module.websocket;

import lombok.extern.slf4j.Slf4j;
import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.sys.common.utils.SpringUtils;
import mayfly.sys.common.websocket.WebSocketUtils;
import mayfly.sys.module.machine.webssh.WebSshService;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author meilin.huang
 * @date 2021-01-07 9:02 上午
 */
@Slf4j
@Component
@ServerEndpoint(WebSshSocket.URI)
public class WebSshSocket {

    public static final String URI = "/machines/{machineId}/terminal/{token}";

    private WebSshService webSshService;

    private PermissionService permissionService;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, @PathParam("machineId") Long machineId, Session session) {
        if (this.webSshService == null) {
            this.webSshService = SpringUtils.getBean(WebSshService.class);
        }
        if (this.permissionService == null) {
            this.permissionService = SpringUtils.getBean(PermissionService.class);
        }
        LoginAccount loginAccount = permissionService.getLoginAccount(token);
        if (loginAccount == null || !"admin".equals(loginAccount.getUsername())) {
            WebSocketUtils.sendText(session, "未登录或非admin用户无权操作");
            return;
        }
        GlobalThreadPool.execute(() -> {
            try {
                webSshService.connectShell(machineId, session);
            } catch (Exception e) {
                WebSocketUtils.sendText(session, String.format("连接失败：%s", e.getMessage()));
                webSshService.close(session);
            }
        });
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSshService.close(session);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        webSshService.recvMsg(message, session);
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误, Session ID： {}", session.getId(), error);
    }
}
