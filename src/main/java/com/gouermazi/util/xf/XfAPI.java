package com.gouermazi.util.xf;


import com.gouermazi.util.file.ResourceUtils;

/**
 * created by tab chan on 2017/12/14
 *
 * 科大讯飞API
 */
public class XfAPI {
    /**
     * 讯飞接口域名
     */
    public static final String XF_BASEURL = "api.xfyun.cn";

    /**
     * 文本语义接口
     */
    public static final String INTERFACE_TEXT_RECOGNITION = "/v1/aiui/v1/text_semantic";

    /**
     * appid
     */
    public static final String appid = ResourceUtils.getString("system","xf.appid");

    /**
     * apikey
     */
    public static final String apikey = ResourceUtils.getString("system","xf.apikey");

    public static final String HTTPS = "https://";
    public static final String HTTP = "http://";
}
