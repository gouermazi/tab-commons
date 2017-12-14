package com.gouermazi.util.xf;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * created by tab chan on 2017/12/14
 */
public class XfRequestUtil {
//    private static final Logger LOGGER = LoggerFactory.getLogger(XfRequestUtil.class);

    /**
     * 注：
     * 1.CheckSum有效期：出于安全性考虑，每个CheckSum的有效期为5分钟(用curTime计算)，
     * 同时CurTime要与标准时间同步，否则，时间相差太大，服务端会直接认为CurTime无效。
     * 2. checkSum生成示例
     * 例如：
     * ApiKey是abcd1234, CurTime是1502607694，Param是eyJzY2VuZSI6Im1haW4ifQ==, http_body是text=5LuK5aSp5pif5pyf5Yeg。
     * 那么CheckSum为MD5(abcd12341502607694eyJzY2VuZSI6Im1haW4ifQ==text=5LuK5aSp5pif5pyf5Yeg)
     * 最终MD5为32位小写 a2fe085df68c87b8aca5f539df8e1a3d
     *
     * @param appid
     * @param appkey
     * @param xParam_json X-Param	Base64编码的json	是	标准JSON格式参数需把参数组装json对象，然后对json进行base64编码
     *                    json: {"scene":"main", "userid":"user_0001"}
     *                    base64编码： eyJzY2VuZSI6Im1haW4iLCAidXNlcmlkIjoidXNlcl8wMDAxIn0=
     * @param nowtime1    毫秒数
     * @return 符合讯飞要求的参数各自放入header中
     */
    public static Collection<Header> getHeaders(String appid, String appkey, String txt_sentence, String xParam_json, long nowtime1) {
        long nowtime = TimeUnit.MILLISECONDS.toSeconds(nowtime1);
        String xParam;
        String httpbody = toBase64(txt_sentence);
        String checkSum = "";
        xParam = toBase64(xParam_json);
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            String toBeMD5 = appkey + nowtime + xParam + "text=" + httpbody;
//            LOGGER.info("toBeMD5 = "+toBeMD5);
            byte[] mdedBytes = md.digest(toBeMD5.getBytes("utf-8"));
            checkSum = md5BytesToHexString(mdedBytes);
        } catch (NoSuchAlgorithmException e) {
//            LOGGER.error("md5加密失败,{}", e.getMessage(), e);
        } catch (UnsupportedEncodingException ue) {
//            LOGGER.error("获取指定编码的字节数组错误,{}", ue.getMessage(), ue);
        }

        Collection<Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("X-Appid", appid));
        headers.add(new BasicHeader("X-CurTime", nowtime + ""));
        headers.add(new BasicHeader("X-CheckSum", checkSum));
        headers.add(new BasicHeader("X-Param", xParam));
        headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        return headers;

    }

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
//            LOGGER.error("base64编码错误,{}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * md5字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String md5BytesToHexString(byte[] bytes) {
        byte[] mdbytes = bytes;
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static JSONObject sendTextRecognitionRequest(String appid, String appkey, String txt_sentence, String xParam_json,String url) {
//        String appid = "5a1e4f93";
//        String appkey = "ad153238982547e7b523e7b96a962286";
//        String txt_sentence = "今天星期几";
//        String xParam_json = "{\"scene\":\"main\", \"userid\":\"user_0001\"}";
        long nowtime = System.currentTimeMillis();


        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        //加请求头
        Collection<Header> headers = getHeaders(appid, appkey,txt_sentence, xParam_json, nowtime);
        Header[] headers1 = headers.toArray(new Header[]{});
        httpPost.setHeaders(headers1);
//        LOGGER.info(Arrays.toString(headers1));
        //加body参数
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("text", toBase64(txt_sentence)));
//        LOGGER.info("text="+toBase64(txt_sentence));

        InputStream is = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder builder = null;
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            response = httpclient.execute(httpPost);
            HttpEntity entity2 = response.getEntity();
            // do something useful with the response body
            is = entity2.getContent();
            //jsonObject处理数据
            // 从输入流读取返回内容
            inputStreamReader = new InputStreamReader(is, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String tmpStr;
            builder = new StringBuilder();
            while ((tmpStr = bufferedReader.readLine()) != null) {
                builder.append(tmpStr);
            }
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } catch (Exception e) {
//            LOGGER.error("发送文本语义识别请求错误,{}", e.getMessage(), e);
        } finally {
            // 释放资源
            try {
                response.close();
            } catch (Exception e) {
//                LOGGER.error("关流失败");
            }
            try {
                is.close();
            } catch (Exception e) {
//                LOGGER.error("关流失败");
            }
            try {
                bufferedReader.close();
            } catch (Exception e) {
//                LOGGER.error("关流失败");
            }
            try {
                inputStreamReader.close();
            } catch (Exception e) {
//                LOGGER.error("关流失败");
            }
        }
        return JSONObject.parseObject(builder.toString());
    }
}
