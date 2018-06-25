package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool jedisPool;
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//最大空闲连接数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//最小空闲连接数
    private static boolean testOnborrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.min.borrow","true"));
    private static boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.min.return","true"));
    private static String ip=PropertiesUtil.getProperty("redis.ip");
    private static Integer port=Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    static{
        initPool();
    }
    private static void initPool(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(testOnborrow);
        config.setMinIdle(minIdle);
        config.setTestOnReturn(testOnReturn);
        jedisPool=new JedisPool(config,ip,port,1000*1);
    }

    public static Jedis getJedisPool() {
        return jedisPool.getResource();
    }
    public static void returnResource(Jedis jedis){
        jedisPool.returnResource(jedis);
    }
    public static void returnBrokenResource(Jedis jedis){
        jedisPool.returnBrokenResource(jedis);
    }
    public static void main(String[] args){
        getJedisPool();
    }
}
