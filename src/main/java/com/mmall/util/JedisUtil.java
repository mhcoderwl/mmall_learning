package com.mmall.util;

import com.mmall.common.RedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtil {
    public static final Logger logger= LoggerFactory.getLogger(FTPUtil.class);
    static public String set(String key,String value){
        Jedis jedis=null;
        String result=null;

        try {
            jedis=RedisPool.getJedisPool();
            result=jedis.set(key,value);
        } catch (Exception e) {
            RedisPool.returnBrokenResource(jedis);
            logger.error("set key:{} value:{} error",key,value);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    static public String setex(String key,String value,Integer extime){
        Jedis jedis=null;
        String result=null;
        try {
            jedis=RedisPool.getJedisPool();
            result=jedis.setex(key,extime,value);
        } catch (Exception e) {
            RedisPool.returnBrokenResource(jedis);
            logger.error("setex key:{} extime:{} value:{} error",key,extime,value);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    /*
    设置key的有效期限
     */
    static public Long expire(String key,Integer extime){
        Jedis jedis=null;
        Long result=null;

        try {
            jedis=RedisPool.getJedisPool();
            result=jedis.expire(key,extime);
        } catch (Exception e) {
            RedisPool.returnBrokenResource(jedis);
            logger.error("expire key:{} extime:{} error",key,extime);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    static public String get(String key){
        Jedis jedis=null;
        String result=null;

        try {
            jedis=RedisPool.getJedisPool();
            result=jedis.get(key);
        } catch (Exception e) {
            RedisPool.returnBrokenResource(jedis);
            logger.error("get key:{} error",key);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    static public Long del(String key){
        Jedis jedis=null;
        Long result=null;

        try {
            jedis=RedisPool.getJedisPool();
            result=jedis.del(key);
        } catch (Exception e) {
            RedisPool.returnBrokenResource(jedis);
            logger.error("del key:{} error",key);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
}
