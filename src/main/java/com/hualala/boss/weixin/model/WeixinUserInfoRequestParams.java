package com.hualala.boss.weixin.model;

/**
 * @author liwenxing
 * @date 2019/1/8 16:49
 *
 * https://work.weixin.qq.com/api/doc#90001/90143/91121
 *
 */
public class WeixinUserInfoRequestParams {

    private String access_token;
    private String code;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
