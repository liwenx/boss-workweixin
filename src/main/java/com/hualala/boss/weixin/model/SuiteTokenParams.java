package com.hualala.boss.weixin.model;

import com.hualala.boss.weixin.Constants.WorkWeixinConstant;

/**
 * @author liwenxing
 * @date 2019/1/9 10:04
 */

public class SuiteTokenParams {
    private String suite_id = WorkWeixinConstant.SUITEID;
    private String suite_secret = WorkWeixinConstant.SUITESECRET;
    private String suite_ticket;

    public String getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(String suite_id) {
        this.suite_id = suite_id;
    }

    public String getSuite_secret() {
        return suite_secret;
    }

    public void setSuite_secret(String suite_secret) {
        this.suite_secret = suite_secret;
    }

    public String getSuite_ticket() {
        return suite_ticket;
    }

    public void setSuite_ticket(String suite_ticket) {
        this.suite_ticket = suite_ticket;
    }
}
