package com.gouermazi.util.encode;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * created by tab chan on 2017/12/14
 */
public class EcnodeUtils {
    /**
     * 对请求体中的参数base64编码
     *
     * @param txt_sentence
     * @return
     */
    public static String toBase64(String txt_sentence) {
        try {
            return Base64.getEncoder().encodeToString(txt_sentence.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("base64加密错误:");
            e.printStackTrace();
            return "";
        }
    }
}
