package mayfly.core.web;

import jakarta.servlet.http.HttpServletResponse;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.IOUtils;
import mayfly.core.util.JsonUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author meilin.huang
 * @date 2021-05-12 3:09 下午
 */
public class WebUtils {

    /**
     * 响应json对象
     *
     * @param response response
     * @param obj      响应对象
     */
    public static void writeJson(HttpServletResponse response, Object obj) {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(JsonUtils.toJSONString(obj));
            response.flushBuffer();
        } catch (Exception e) {
            throw BizAssert.newException("响应json异常: %s", e);
        }
    }

    /**
     * 文件流下载，自动关闭输入流
     *
     * @param response response
     * @param in       输入流
     */
    public static void downloadStream(HttpServletResponse response, String filename, InputStream in) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        try {
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            throw BizAssert.newException("文件下载-读取字节失败");
        } finally {
            IOUtils.close(in);
        }
    }

    /**
     * 文件流下载，适合数据量较小
     *
     * @param response response
     * @param bytes    文件字节数组
     */
    public static void downloadStream(HttpServletResponse response, String filename, byte[] bytes) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
        try {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            throw BizAssert.newException("文件下载-读取字节失败");
        } finally {
            IOUtils.close(bis);
        }
    }
}
