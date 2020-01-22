package mayfly.core.permission;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-22 09:26
 */
public class SessionLocal<T> {

    private static ThreadLocal idLocal = new ThreadLocal<>();

    public static <T> void setUserId(T id) {
        idLocal.set(id);
    }

    public static <T> T getUserId() {
        return (T)idLocal.get();
    }

    public static void remove() {
        idLocal.remove();
    }
}
