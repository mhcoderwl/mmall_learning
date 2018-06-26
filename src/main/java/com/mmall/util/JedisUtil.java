package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

public class JedisUtil {
    public static final Logger logger= LoggerFactory.getLogger(JedisUtil.class);
    static public String set(String key,String value){
        ShardedJedis jedis=null;
        String result=null;

        try {
            jedis=RedisShardedPool.getJedisPool();
            result=jedis.set(key,value);
        } catch (Exception e) {
            RedisShardedPool.returnBrokenResource(jedis);
            logger.error("set key:{} value:{} error",key,value);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    static public String setex(String key,String value,Integer extime){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis=RedisShardedPool.getJedisPool();
            result=jedis.setex(key,extime,value);
        } catch (Exception e) {
            RedisShardedPool.returnBrokenResource(jedis);
            logger.error("setex key:{} extime:{} value:{} error",key,extime,value);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    /*
    设置key的有效期限
     */
    static public Long expire(String key,Integer extime){
        ShardedJedis jedis=null;
        Long result=null;

        try {
            jedis=RedisShardedPool.getJedisPool();
            result=jedis.expire(key,extime);
        } catch (Exception e) {
            RedisShardedPool.returnBrokenResource(jedis);
            logger.error("expire key:{} extime:{} error",key,extime);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    static public String get(String key){
        ShardedJedis jedis=null;
        String result=null;

        try {
            jedis=RedisShardedPool.getJedisPool();
            result=jedis.get(key);
        } catch (Exception e) {
            RedisShardedPool.returnBrokenResource(jedis);
            logger.error("get key:{} error",key);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    static public Long del(String key){
        ShardedJedis jedis=null;
        Long result=null;

        try {
            jedis=RedisShardedPool.getJedisPool();
            result=jedis.del(key);
        } catch (Exception e) {
            RedisShardedPool.returnBrokenResource(jedis);
            logger.error("del key:{} error",key);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
}
