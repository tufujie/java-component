package com.jef.util;

import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by SUNLQ on 17/2/13.
 */
public class RSAUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * 生成签名
     * @param priKeyValue
     * @param signSrc
     * @return
     */
    public String signed(String priKeyValue, String signSrc) {
        byte[] sign = this.signature(priKeyValue, signSrc);
        return sign == null?"":new String(org.apache.commons.codec.binary.Base64.encodeBase64(sign));
    }

    /**
     * 签名验证
     * @param pubKeyValue
     * @param oriStr
     * @param signedStr
     * @return
     */
    public boolean verify(String pubKeyValue, String oriStr, String signedStr) {
        try {
            X509EncodedKeySpec e = new X509EncodedKeySpec(getBytesBASE64(pubKeyValue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(e);
            byte[] signed = getBytesBASE64(signedStr);
            Signature signetcheck = Signature.getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oriStr.getBytes(CHARSET));
            return signetcheck.verify(signed);
        } catch (Exception var9) {
            return false;
        }
    }

    private byte[] signature(String priKeyValue, String signSrc) {
        try {
            PKCS8EncodedKeySpec e = new PKCS8EncodedKeySpec(getBytesBASE64(priKeyValue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(e);
            Signature signet = Signature.getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(signSrc.getBytes(CHARSET));
            return signet.sign();
        } catch (Exception var7) {
            return null;
        }
    }

    private static byte[] getBytesBASE64(String s) {
        if(s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] e = decoder.decodeBuffer(s);
                return e;
            } catch (Exception var3) {
                return null;
            }
        }
    }
}
