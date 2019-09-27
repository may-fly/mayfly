package mayfly.core.permission;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-22 09:26
 */
public class SessionLocal {

    private static ThreadLocal<Integer> idLocal = new ThreadLocal<>();

    public static void setUserId(Integer id) {
        idLocal.set(id);
    }

    public static Integer getUserId() {
        return idLocal.get();
    }

    public static void remove() {
        idLocal.remove();
    }
}
