package com.gouermazi.util.sig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * created by tab chan on 2017/11/29
 */
public class SignatureUtil {
    public static String textCurl(String appid, String appkey, String txt_sentence, String xParam_json) {
        long nowtime = System.currentTimeMillis();
        nowtime = TimeUnit.MILLISECONDS.toSeconds(nowtime);
        String xParam = "";
        String httpbody = "";
        String checkSum = "";
        String baseurl = "api.xfyun.cn";
        String inf = "/v1/aiui/v1/text_semantic";
        try {
            httpbody = Base64.getEncoder().encodeToString(txt_sentence.getBytes("utf-8"));
            xParam = Base64.getEncoder().encodeToString(xParam_json.getBytes("utf-8"));
            System.out.println("httpbody=  " + httpbody);
            System.out.println("xparam=    " + xParam);
        } catch (UnsupportedEncodingException e) {
            System.out.println("base64加密错误:");
            e.printStackTrace();
        }
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            String toBeMD5 = appkey + nowtime + xParam + "text=" + httpbody;
            System.out.println("toBeMD5=        " + toBeMD5);
            try {
                byte[] mdedBytes = md.digest(toBeMD5.getBytes("utf-8"));
                checkSum = md5BytesToHexString(mdedBytes);
                System.out.println("checkSum=    " + checkSum);
            } catch (UnsupportedEncodingException e) {
                System.out.println("base64加密错误:");
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("md5加密失败:");
            e.printStackTrace();
        }

        System.out.println("===================================\nALL I NEED IS = \n{" +
                "\n" +
                "   X-Appid     " + appid +
                "\n" +
                "   X-CurTime   " + nowtime +
                "\n" +
                "   X-Param     " + xParam +
                "\n" +
                "   X-CheckSum  " + checkSum +
                "\n" +
                "   text        " + httpbody
                + "\n}\n====================================");
        System.out.println("CURL COMMAND IS \n" +
                "curl -iv  -XPOST http://" + baseurl + inf +
                " -H \"X-Appid:" + appid + "\" -H \"X-CurTime:" + nowtime + "\" " +
                " -H \"X-CheckSum:" + checkSum + "\"" +
                " -H \"X-Param:" + xParam + "\"" +
                " -d \"text=" + httpbody + "\"");
        String curl = "curl -iv  -XPOST http://" + baseurl + inf +
                " -H  X-Appid:" + appid + "  -H  X-CurTime:" + nowtime + "  " +
                " -H  X-CheckSum:" + checkSum + " " +
                " -H  X-Param:" + xParam + " " +
                " -d  text=" + httpbody + " ";
        return curl;
    }

    public static String md5BytesToHexString(byte[] bytes) {
        byte[] mdbytes = bytes;

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Digest(in hex format):: " + sb.toString());
        return sb.toString();
    }
}
