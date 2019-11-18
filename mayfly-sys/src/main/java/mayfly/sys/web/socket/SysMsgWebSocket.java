package mayfly.sys.web.socket;

import lombok.extern.slf4j.Slf4j;
import mayfly.core.result.ResultEnum;
import mayfly.core.util.websocket.WebSocketUtils;
import mayfly.sys.common.utils.SpringUtils;
import mayfly.sys.common.websocket.MessageTypeEnum;
import mayfly.sys.interceptor.PermissionCheckHandlerService;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * 系统消息
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-09-06 4:07 下午
 */
@Slf4j
@Component
@ServerEndpoint(SysMsgWebSocket.URI)
public class SysMsgWebSocket {

    public static final String URI = "/sysmsg/{token}";

    private static WebSocketUtils.SessionRegistry<String> registry = WebSocketUtils.SessionRegistry.create(URI, true);
    static {
        WebSocketUtils.putRegistry(registry);
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        Integer userId = SpringUtils.getBean(PermissionCheckHandlerService.class).getIdByToken(token);
        if (userId == null) {
            WebSocketUtils.sendText(session, MessageTypeEnum.SYS_NOTIFY.error(ResultEnum.NO_PERMISSION));
            return;
        }
        registry.putSession(session.getId(), session);
        WebSocketUtils.sendText(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        registry.removeSession(session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        sendMessage(session, "收到消息，消息内容："+message);
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }
}
