package com.jef.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class XingMing {

    static final Logger log = Logger.getLogger(XingMing.class);

    public static String read(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            StringBuffer buf = new StringBuffer();
            String line = null;
            while ((line = read.readLine()) != null) {
                buf.append(line);
            }
            return buf.toString();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static String find(String str, String beginStr, String endStr) {
        final int length = beginStr.length();
        int index = str.indexOf(beginStr);
        String result = null;
        if (index != -1) {
            int index2 = str.indexOf(endStr, index + length);
            if (index2 != -1) {
                result = str.substring(index + beginStr.length(), index2);
            }
        }
        return result;
    }

    public static String findName(String source, String name) {
        // value=我的姓名『XX』的分析：
        return find(source, "value=我的姓名『", "』的分析");
    }

    public static String findScore(String source, String name) {
        // <font size=3>姓名评分：</font><font color=0000ff size=5 face="Broadway
        // BT,楷体">99.5</font>
        return find(
                source,
                "<font size=3>姓名评分：</font><font color=0000ff size=5 face='Broadway BT,楷体'>",
                "</font>");
    }

    public static String findHZ(String source, String name) {
        return find(source, name + " </font></td><td><font color=aaaaaa>",
                "</font>");
    }

    public static String findSound(String source, String name) {
        return find(source, name + "</font></td><td><font color=aaaaaa>",
                "</font>");
    }

    public static String findWuXing(String source, String sound) {
        // <font color=aaaaaa>qian</font></td><td>12</td><td>木</td></tr>
        String s1 = find(source, "<font color=aaaaaa>" + sound
                + "</font></td><td>", "</td>");
        return find(source, "<font color=aaaaaa>" + sound + "</font></td><td>"
                + s1 + "</td><td>", "</td>");
    }

    public static int countStr(String source, String str) {
        int cnt = 0;
        int idx = source.indexOf(str);
        while (idx != -1) {
            cnt++;
            idx = source.indexOf(str, idx + str.length());
        }
        return cnt;
    }

    public static void main(String[] args) throws IOException {
        final char firstChar = '一';
        final char lastChar = '涂';
        for (char i = firstChar; i < lastChar; i++) {
            // System.out.print(i);
        }
        // 最大开启100个线程，可以加快查询速度.
        int maxThread = 100;
        int step = (lastChar - firstChar) / maxThread;
        for (int i = 0; i < maxThread; i++) {
            char start = (char) (firstChar + i * step);
            char end = (char) (firstChar + i * step + step - 1);
            System.out.println("开启" + (i + 1) + "处理:" + start + "-" + end
                    + (char) (end + 1));
            new CallThread(start, end).start();
        }
    }

    static class CallThread extends Thread {
        private char start;
        private char end;
        private String info;

        CallThread(char start, char end) {
            this.start = start;
            this.end = end;
            this.info = this.start + "-" + this.end;
        }

        public void run() {
            // 姓
            final char youname1 = '涂';
            final String url = "http://www.xingming.net/cmjg-mz.asp?sex=女&youname1="
                    + youname1 + "&youname2=";
            String youname2;
            String webinfo = null;
            for (char i = start; i <= end; i++, webinfo = null) {
                // 名字规则自己取吧.
                // youname2 = "良" + i;
                // youname2 = "" + i + i;
                youname2 = "东" + i;
                for (int j = 0; j < 5 && webinfo == null; j++) {
                    webinfo = XingMing.read(url + youname2);
                }
                if (webinfo == null) {
                    log.warn("获取名字[" + youname1 + youname2 + "]失败");
                    continue;
                }
                String webName = XingMing.findName(webinfo, "[" + youname2
                        + "]");
                String webScore = XingMing.findScore(webinfo, "[" + youname2
                        + "]");
                String webHZ = XingMing.findHZ(webinfo, "" + i);
                String webSound = XingMing.findSound(webinfo, webHZ);
                String webWuxing = XingMing.findWuXing(webinfo, webSound);
                String 天格数 = find(webinfo, "天格-> ", "(");
                String 人格数 = find(webinfo, "人格-> ", "(");
                String 地格数 = find(webinfo, "地格-> ", "(");
                String 外格数 = find(webinfo, "外格-> ", "(");
                String 总格数 = find(webinfo, "总格-> ", "(");
                String 天格解析 = find(webinfo, "天格" + 天格数 + "的解析</td>  <td>（", "）");
                String 人格解析 = find(webinfo, "人格" + 人格数 + "的解析</td>  <td>（", "）");
                String 地格解析 = find(webinfo, "地格" + 地格数 + "的解析</td>  <td>（", "）");
                String 外格解析 = find(webinfo, "外格" + 外格数 + "的解析</td>  <td>（", "）");
                String 总格解析 = find(webinfo, "总格" + 总格数 + "的解析</td>  <td>（", "）");

                String 天格暗示 = find(webinfo, "天格" + 天格数 + "有以下数理暗示</td><td>",
                        "</td>");
                String 人格暗示 = find(webinfo, "人格" + 人格数 + "有以下数理暗示</td><td>",
                        "</td>");
                String 地格暗示 = find(webinfo, "地格" + 地格数 + "有以下数理暗示</td><td>",
                        "</td>");
                String 外格暗示 = find(webinfo, "外格" + 外格数 + "有以下数理暗示</td><td>",
                        "</td>");
                String 总格暗示 = find(webinfo, "总格" + 总格数 + "有以下数理暗示</td><td>",
                        "</td>");

                String 天人地 = find(webinfo, "对三才数理的影响(天人地)</td>  <td>", "</td>");
                String 人地 = find(webinfo, "对基础运的影响(人地)</td><td>", "</td>");
                String 天人 = find(webinfo, "对成功运的影响(天人)</td><td>", "</td>");
                String 人外 = find(webinfo, "对人际关系的影响(人外)</td><td>", "</td>");

                int lackCnt = XingMing.countStr(webinfo, "（吉）");
                int lackCnt2 = XingMing.countStr(webinfo, "（半吉）");
                try {
                    if (Float.parseFloat(webScore) >= 95) {
                        System.out.println(
                                webName + " " + webScore + " "
                                        + webHZ + " " + webSound + " " + lackCnt
                                        + " " + lackCnt2 + " " + webWuxing + " "
                                        + 天格数 + " " + 人格数 + " " + 地格数 + " " + 外格数 + " " + 总格数 + " " //
                                        + 天格解析 + " " + 人格解析 + " " + 地格解析 + " " + 外格解析 + " " + 总格解析 + " "//
                                        + 天格暗示 + " " + 人格暗示 + " " + 地格暗示 + " " + 外格暗示 + " " + 总格暗示 + " " //
                                        + 天人地 + " " + 人地 + " " + 天人 + " " + 人外);
                    }
                } catch (Exception e) {
                }
                log.info(this.info + ":" + webName + ":" + webScore + ":"
                        + lackCnt + ":" + lackCnt2);
                if ((i - start) % 100 == 0) {
                    System.out.println(this.info + "处理了" + (i - start) + "个");
                }
            }
            System.out.println(this.info + "结束了.....");
        }
    }
}
