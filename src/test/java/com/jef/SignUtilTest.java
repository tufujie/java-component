package com.jef;

import com.jef.util.SignUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tufujie
 * @date 2023/12/21
 */
public class SignUtilTest {

    @Test
    void testCreateLinkString() {
        Map<String, String> params = new HashMap<>();
        params.put("a", "testa");
        params.put("b", "testb");
        System.out.println(SignUtil.createLinkString(params));
    }

}