package com.hualala.boss.weixin.Constants;

/**
 * @author liwenxing
 * @date 2019/1/3 15:37
 */
public class WorkWeixinConstant {

//    public static final String STOKEN = "QDG6eK";
//    public static final String SCORPID = "wx5823bf96d3bd56c7";
//    public static final String SENCODINGAESKEY = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";

    public static final String STOKEN = "eq1rGX";
    public static final String SCORPID = "ww7b6130438bcaae19"; // 企业ID
    public static final String SENCODINGAESKEY = "qotsDsBqSOaflLOvIeO5AVKo0CluV9JPErwIkUpwEwZ";
    public static final String SUITEID = "ww1905b263c9cd581e"; //第三方应用ID
    public static final String SUITESECRET = "OccXOwz0SYISQI20a-hPscJTsk9zsRThC2-aUeKPsY4";

//    public static final String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    public static final String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    public static final String WECHAT_REDIRECT = "#wechat_redirect";
    // https://qyapi.weixin.qq.com/cgi-bin/service/getuserinfo3rd?access_token=SUITE_ACCESS_TOKEN&code=CODE
    public static final String USER_INFO_REQ = "https://qyapi.weixin.qq.com/cgi-bin/service/getuserinfo3rd";

    public static final  String GET_SUITE_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token";
}
