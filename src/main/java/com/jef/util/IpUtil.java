package com.jef.util;

/**
 * IP处理工具
 * @author Jef
 * @date 2018/9/14 17:34
 */
public class IpUtil {
    /**
     * IP的.分割数
     */
    private static final int IPSPLIT_NUM = 3;

    /**
     * 字符型IP，用于数据库存储
     * (Ip转Integer)
     * @exception
     */
    public static int ipToInteger(String ip){
        String[] ips = ip.split("\\.");
        int ipFour = 0;
        // 因为每个位置最大255，刚好在2进制里表示8位
        for(String ip4: ips){
            Integer ip4a = Integer.parseInt(ip4);
            // 这里应该用+也可以,但是位运算更快
            ipFour = (ipFour << 8) | ip4a;
        }
        return ipFour;
    }

    /**
     * (Integer转IP)，用于读取
     * @param ip 整型IP
     * @return String
     * @exception
     */
    public static String IntegerToIp(Integer ip){
        // 思路很简单，每8位拿一次，就是对应位的IP
        StringBuilder sb = new StringBuilder();
        for(int i = IPSPLIT_NUM; i >= 0; i--){
            int ipa = (ip >> (8 * i)) & (0xff);
            sb.append(ipa + ".");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
