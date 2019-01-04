package mayfly.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-29 4:49 PM
 */
public final class LocalUtils {

    /**
     * 获取本地ip地址
     * @return
     */
    public static String getLocalIp(){
        try{
            //根据网卡取本机配置的IP
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            String serverIp = null;
            while(netInterfaces.hasMoreElements()){
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        serverIp =  ip.getHostAddress();
                        return serverIp;
                    }
                }
            }
        }catch (Exception e){
           return null;
        }
        throw new RuntimeException("获取ip错误!");
    }
}
