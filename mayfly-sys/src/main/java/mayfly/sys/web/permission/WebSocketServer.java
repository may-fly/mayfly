package mayfly.sys.web.permission;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-27 6:54 PM
 */
@Component
@ServerEndpoint("/logs")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnClose
    public void onClose() {

    }
}
