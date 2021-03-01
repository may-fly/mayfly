package mayfly.sys.module.machine.webssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import lombok.extern.slf4j.Slf4j;
import mayfly.core.util.JsonUtils;
import mayfly.sys.common.utils.ssh.SSHUtils;
import mayfly.sys.common.websocket.WebSocketUtils;
import mayfly.sys.module.machine.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @date 2021-01-06 3:54 下午
 */
@Slf4j
@Service
public class WebSshService {

    @Autowired
    private MachineService machineService;

    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public void recvMsg(String buffer, Session session) {
        WebSshRecvMsg msg = JsonUtils.parseObject(buffer, WebSshRecvMsg.class);
        if (msg.getType().equals(WebSshRecvMsg.CMD)) {
            Channel channel = CHANNEL_MAP.get(session.getId());
            try {
                trans2Ssh(channel, msg.getCmd());
            } catch (Exception e) {
                log.error("执行命令 :'{}' 失败", msg.getCmd(), e);
                close(session);
            }
            return;
        }

        if (msg.getType().equals(WebSshRecvMsg.RESIZE)) {
            ChannelShell channel = (ChannelShell) CHANNEL_MAP.get(session.getId());
            if (channel != null) {
                channel.setPtySize(msg.getCols(), msg.getRows(), msg.getCols(), msg.getRows());
            }
        }
    }

    public void connectShell(Long machineId, Session webSocketSession) throws Exception {
        ChannelShell channel = SSHUtils.openChannelShell(machineService.getSession(machineId));
        //通道连接 超时时间3s
        channel.connect(5000);
        CHANNEL_MAP.put(webSocketSession.getId(), channel);
        //读取终端返回的信息流
        try (InputStream inputStream = channel.getInputStream()) {
            //循环读取
            byte[] buffer = new byte[1024];
            int i;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            //断开连接后关闭会话
            channel.disconnect();
        }

    }

    /**
     * 发送消息至客户端
     *
     * @param session session
     * @param buffer  内容
     */
    public void sendMessage(Session session, byte[] buffer) throws IOException {
        WebSocketUtils.sendText(session, new String(buffer));
    }

    public void close(Session session) {
        String id = session.getId();
        Channel channel = CHANNEL_MAP.get(id);
        if (channel != null) {
            channel.disconnect();
        }
        CHANNEL_MAP.remove(id);
    }

    /**
     * 将消息转发到终端
     *
     * @param channel channel
     * @param command cmd
     * @throws IOException exception
     */
    private void trans2Ssh(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
}
