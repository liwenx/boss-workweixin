package com.hualala.boss.weixin.model;

import com.hualala.boss.weixin.Constants.WorkWeixinConstant;

/**
 * @author liwenxing
 * @date 2019/1/7 15:58
 *
 * 解释说明：
 * https://work.weixin.qq.com/api/doc#90001/90143/91120
 *
 */

public class Oauth2Params {
    private String appid = WorkWeixinConstant.SUITEID;
    private String redirect_uri;
    private String response_type = "code";
    private String scope = "snsapi_base";
    private String state = "boss";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
