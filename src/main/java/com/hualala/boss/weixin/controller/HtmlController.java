package com.hualala.boss.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.hualala.boss.weixin.Constants.WorkWeixinConstant;
import com.hualala.boss.weixin.model.Oauth2Params;
import com.hualala.boss.weixin.model.WeixinUserInfoRequestParams;
import com.hualala.boss.weixin.util.WeixinUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author liwenxing
 * @date 2018/5/16 18:22
 */
@Controller
public class HtmlController {

    @Autowired
    private WeixinUtil weixinUtil;

    /**
     * 新版老板通后台管理页 vue
     * @return
     */
    @RequestMapping(value = "/manage/**", method = RequestMethod.GET)
    public String getManagementHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1. 判断 Cookie 成功则直接访问页面 Cookie失效
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if("token".equals(cookie.getName())) {
                    // 校验Cookie有效性
                    return "forward:/managementDist/index.html";
                }
            }
        }
        //2. Cookie 无效 则判断是否有CODE 有CODE 则从CODE中取出userid 请求passport并得到Cookie信息  得到正确Cookie信息后 访问页面
        String code = request.getParameter("code");
        //3. 没有CODE信息 重定向至企业微信服务器链接获取CODE
        if (StringUtils.isEmpty(code)) {
            Oauth2Params oauth2Params = new Oauth2Params();
            oauth2Params.setRedirect_uri(URLEncoder.encode(request.getRequestURL().toString(), "utf-8"));

            return "redirect:" + WorkWeixinConstant.CODE_URL + "?" + WeixinUtil.parseURLPair(oauth2Params) + WorkWeixinConstant.WECHAT_REDIRECT;
//            return "forward:/managementDist/index.html";
        } else {

            // 取出 code 请求微信服务器得到userid 请求passport并得到Cookie信息 并转发至前端页面
            WeixinUserInfoRequestParams weixinUserInfoRequestParams = new WeixinUserInfoRequestParams();
            weixinUserInfoRequestParams.setCode(code);
            weixinUserInfoRequestParams.setAccess_token(weixinUtil.getSuiteAccessToken());
            String userURL = WorkWeixinConstant.USER_INFO_REQ  + "?" + WeixinUtil.parseURLPair(weixinUserInfoRequestParams);
            Request userRequest = new Request.Builder().url(userURL).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Response userResponse = okHttpClient.newCall(userRequest).execute();
            if (userResponse.isSuccessful()) {
                Map<String, Object> map = JSON.parseObject(userResponse.body().string(), Map.class);
                String userId = map.get("UserId").toString();
                System.out.println("UserId: " + userId);
                Cookie cookie = new Cookie("token", userId);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return "forward:/managementDist/index.html";
        }
    }

}
