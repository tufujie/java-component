package com.jef.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 打印内容
 *
 * @author Jef
 * @date 2019/5/21
 */
public class PrintConstants {

    public static Map<String, Object> printMap = new HashMap<String, Object>() {
        private static final long serialVersionUID = -5408097675099155670L;
        {
            put("{用户名}", "userName");
            put("{用户手机}", "userPhone");
            put("{用户年龄}", "userAge");
        }
    };

}