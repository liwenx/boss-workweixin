package com.hualala.boss.weixin.util;

import com.alibaba.fastjson.JSON;
import com.hualala.boss.weixin.Constants.WorkWeixinConstant;
import com.hualala.boss.weixin.model.SuiteTokenParams;
import com.hualala.boss.weixin.service.inter.RedisService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author liwenxing
 * @date 2019/1/8 11:03
 */
@Component
public class WeixinUtil {

    @Autowired
    RedisService redisService;

    public static String parseURLPair(Object o) throws Exception {
        Class<? extends Object> c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        Map<String, Object> map = new TreeMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(o);
            if (value != null)
                map.put(name, value);
        }
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> it = set.iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, Object> e = it.next();
            sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 实体类转json
     * @param obj
     * @return
     */
    public static String jsonFormat(Object obj){
        Class c = obj.getClass();
        Field fields[] = c.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        try {
            for(int i=0; i<fields.length; i++){
                String name = fields[i].getName();
                fields[i].setAccessible(true);
                String value = fields[i].get(obj).toString();
                sb.append("\""+name+"\":"+"\""+value+"\"");
                if (i < fields.length -1) {
                    sb.append(",");
                }
            }
        } catch (Exception e) {
        }
        sb.append("}");
        return sb.toString();
    }

    public  String getSuiteAccessToken() throws Exception {
        String result = null;
        String suiteTicket = redisService.getStringValue(RedisUtils.BOSS_WORKWEIXIN_SUITETICKET);
        SuiteTokenParams suiteTokenParams = new SuiteTokenParams();
        suiteTokenParams.setSuite_ticket(suiteTicket);
        String url = WorkWeixinConstant.GET_SUITE_TOKEN;
        MediaType JSONType = MediaType.parse("application/json;charset=utf-8");
        String reqJson = jsonFormat(suiteTokenParams);
        RequestBody requestBody = RequestBody.create(JSONType, reqJson);
        Request scmRequest = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json;charset:utf-8")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(scmRequest).execute();
        if (response.isSuccessful()) {
            Map<String, Object> map = JSON.parseObject(response.body().string(), Map.class);
            System.out.println(map.toString());
            result = map.get("suite_access_token").toString();
        }
        return result;
    }

}
