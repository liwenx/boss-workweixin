package com.hualala.boss.weixin.model;

/**
 * @author liwenxing
 * @date 2019/1/3 15:34
 */
//@Data
public class CallBackModel {
    private String msg_signature; // 企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体。
    private String timestamp; // 时间戳。与nonce结合使用，用于防止请求重放攻击。
    private String nonce; // 随机数。与timestamp结合使用，用于防止请求重放攻击。
    private String echostr; // 加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文

    public String getMsg_signature() {
        return msg_signature;
    }

    public void setMsg_signature(String msg_signature) {
        this.msg_signature = msg_signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }
}
