package com.hualala.boss.weixin.service.impl;

import com.hualala.boss.weixin.service.inter.RedisService;
import com.hualala.boss.weixin.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * laobantong_api - com.hualala.laobantong.service.laobantong.impl
 *
 * @author liyanlong
 * @date 2017/11/7 15:45.
 * @since JDK 1.8
 */
@Service
public class RedisServiceImpl implements RedisService {

	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public boolean putPenetrateMobile(String mobile) {

		try {
			if(mobile == null) {
				return false;
			}
			redisTemplate.opsForSet().add(RedisUtils.MOBILE_REDIS_SET, mobile);
			return true;
		} catch (Exception e) {
			logger.error("putPenetrateMobile error:", e);
		}
		return false;
	}

	@Override
	public boolean checkPenetrateMobile(String mobile) {

		try {
			Set<String> set = redisTemplate.opsForSet().members(RedisUtils.MOBILE_REDIS_SET);
			if(set == null) {
				return false;
			}
			if(set.contains(mobile)) {

				return true;
			}
		} catch (Exception e) {
			logger.error("checkPenetrateMobile error:", e);
		}
		return false;
	}

	/*@Override
	public Set<String> getPenetrateMobile() {

		try {
			Set<String> set = redisTemplate.opsForSet().members(MOBILE_REDIS_SET);
			return set;
		} catch (Exception e) {
			logger.error("getPenetrateMobile error:", e);
		}
		return null;
	}*/

	@Override
	public boolean checkSession(String token) {
		try {
			Object object = redisTemplate.opsForValue().get(token);
			if(object == null) {
				return false;
			}
			//更新token日期
			setValueForTime(token, String.valueOf(object), 60 * 5);
			return true;
		} catch (Exception e) {
			logger.error("checkSession error:", e);
		}
		return false;
	}

	@Override
	public boolean setValue(String key, Object value) {

		try {
			redisTemplate.opsForValue().set(key, value.toString());
			return true;
		} catch (Exception e) {
			logger.error("setValue error:", e);
		}
		return false;
	}

	@Override
	public Object getValue(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("getValue error:", e);
		}
		return null;
	}

	@Override
	public boolean deleteValue(String key) {

		try {
			redisTemplate.delete(key);
			return true;
		} catch (Exception e) {
			logger.error("deleteValue error:", e);
		}
		return false;
	}

	@Override
	public boolean setValueForTime(String key, String value, long minute) {

		try {
			redisTemplate.opsForValue().set(key, value, minute, TimeUnit.MINUTES);
			return true;
		} catch (Exception e) {
			logger.error("setValue error:", e);
		}
		return false;
	}

	@Override
	public Set<String> getSetValues(String key) {

		try {
			Set<String> set = redisTemplate.opsForSet().members(key);
			return set;
		} catch (Exception e) {
			logger.error("getSetValues error:", e);
		}
		return null;
	}

	@Override
	public String getStringValue(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("getValue error:", e);
		}
		return null;
	}

    @Override
    public boolean setNx(String key, long expired) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {

                long millisecondExpired = expired * 1000;
                // 获取锁
                long expireAt = System.currentTimeMillis() + millisecondExpired + 1;
                boolean result = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
                if(result) {
                    connection.expire(key.getBytes(), expired);
                    return result;
                } else {
                    // 查看锁是否过期
                    byte[] valueByte = connection.get(key.getBytes());
                    if(Objects.nonNull(valueByte)) {
                        long expireTime = Long.parseLong(new String(valueByte));
                        if(expireTime < System.currentTimeMillis()) {
                            byte[] oldValue = connection.getSet(key.getBytes(), String.valueOf(System.currentTimeMillis() + millisecondExpired + 1).getBytes());
                            connection.expire(key.getBytes(), expired);
                            return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                        }
                    }
                }
                return false;
            }
        });
        return result;
    }
}
