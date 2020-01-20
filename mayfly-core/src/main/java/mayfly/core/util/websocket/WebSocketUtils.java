package mayfly.core.util.websocket;

import mayfly.core.util.Assert;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 4:51 下午
 */
public class WebSocketUtils {

    private static Map<String, SessionRegistry<?>> registryMap = new ConcurrentHashMap<>();

    /**
     * put registry
     *
     * @param registry SessionRegistry
     * @param <T>      key类型
     */
    public static <T> void putRegistry(SessionRegistry<T> registry) {
        registryMap.put(registry.namespace, registry);
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session session
     * @param message 消息
     */
    @SuppressWarnings("unchecked")
    public static <T> void sendText(Session session, String message) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param namespace 命名空间(区别不同的websocket，可用uri表示)
     * @param key       存储session的key（可以是sessionId，也也是用户标识id等）
     * @param message   文本消息
     */
    @SuppressWarnings("all")
    public static <T> void sendText(String namespace, T key, String message) throws SessionNoFoundException {
        SessionRegistry sessionRegistry = registryMap.get(namespace);
        isExistRegistry(namespace, sessionRegistry);
        Session session = sessionRegistry.getSession(key);
        if (session == null) {
            throw new SessionNoFoundException("session不存在");
        }
        sendText(session, message);
    }

    /**
     * 广播文本消息
     *
     * @param namespace 命名空间
     * @param message   消息
     * @param <T>       存储session的key（可以是sessionId，也也是用户标识id等）
     */
    @SuppressWarnings("all")
    public static <T> void broadcastText(String namespace, String message) {
        SessionRegistry sessionRegistry = registryMap.get(namespace);
        // 判断是否存在该registry
        isExistRegistry(namespace, sessionRegistry);
        sessionRegistry.sessionMap.forEach((k, v) -> {
            try {
                sendText(namespace, k, message);
            } catch (SessionNoFoundException e) {
                // skip，不会发生因为就是遍历 session map
            }
        });
    }

    /**
     * 判断是否存在该registry
     *
     * @param namespace
     * @param registry
     */
    private static void isExistRegistry(String namespace, SessionRegistry registry) {
        Assert.notNull(registry, () -> String.format("不存在%s的websocket session registry，请使用WebSocketUtils的putRegistry方法注册SessionRegistry", namespace));
    }


    /**
     * websocket session注册器（即用来保存、获取以及删除session的）
     *
     * @param <T> session对应key的类型
     */
    public static class SessionRegistry<T> {

        /**
         * 命名空间（url即可）
         */
        private final String namespace;

        /**
         * map中的key是否为sessionId(是的话在移除时可以直接移除，否则遍历map找出对应key再删除)
         */
        private final boolean keyIsSessionId;

        private Map<T, Session> sessionMap = new ConcurrentHashMap<>();


        private SessionRegistry(String namespace, boolean keyIsSessionId) {
            this.namespace = namespace;
            this.keyIsSessionId = keyIsSessionId;
        }

        /**
         * 获取session
         *
         * @param key 存储session的key（可以是sessionId，也也是用户标识id等）
         * @return session
         */
        public Session getSession(T key) {
            return sessionMap.get(key);
        }

        public void putSession(T key, Session session) {
            if (keyIsSessionId && !(key instanceof String)) {
                throw new RuntimeException("sessionId对应的key必须为String类型");
            }
            sessionMap.put(key, session);
        }

        /**
         * 移除session
         *
         * @param sessionId sessionId
         */
        public void removeSession(String sessionId) {
            // 如果该registry map中的key是sessionId的话，则直接移除即可，否则遍历map找出对应key再删除
            if (this.keyIsSessionId) {
                sessionMap.remove(sessionId);
            } else {
                for (Map.Entry<T, Session> entry : sessionMap.entrySet()) {
                    if (Objects.equals(sessionId, entry.getValue().getId())) {
                        sessionMap.remove(entry.getKey());
                        return;
                    }
                }
            }
        }

        /**
         * 创建SessionRegistry对象
         *
         * @param namespace      命名空间（url即可）
         * @param keyIsSessionId map中的key是否为sessionId(是的话在移除时可以直接移除，否则需要遍历map找出对应key再删除)
         * @param <T>            key类型
         * @return SessionRegistry
         */
        public static <T> SessionRegistry<T> create(String namespace, boolean keyIsSessionId) {
            return new SessionRegistry<T>(namespace, keyIsSessionId);
        }
    }
}
