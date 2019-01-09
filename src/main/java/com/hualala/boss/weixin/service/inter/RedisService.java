package com.hualala.boss.weixin.service.inter;

import java.util.Set;

/**
 * laobantong_api - com.hualala.laobantong.service.laobantong.inter
 *
 * @author liyanlong
 * @date 2017/11/7 15:45.
 * @since JDK 1.8
 */
public interface RedisService {

	boolean putPenetrateMobile(String mobile);
	boolean checkPenetrateMobile(String mobile);
	//Set<String> getPenetrateMobile();

	/**
	 * 判断token是否有效，并重置失效时间
	 * @param token
	 * @return
	 */
	boolean checkSession(String token);

	boolean setValue(String key, Object value);

	Object getValue(String key);

	String getStringValue(String key);

	boolean deleteValue(String key);

	boolean setValueForTime(String key, String value, long time);

	Set<String> getSetValues(String key);

    /**
     * redis获取唯一的key
     * @param key
     * @param expired key存在的时间 单位s
     * @return
     */
	boolean setNx(String key, long expired);
}
