package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool shardedJedisPool;
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//最大空闲连接数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//最小空闲连接数
    private static boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.min.borrow","true"));
    private static boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.min.return","true"));
    private static String ip1=PropertiesUtil.getProperty("redis.ip1");
    private static Integer port1=Integer.parseInt(PropertiesUtil.getProperty("redis.port1"));
    private static String ip2=PropertiesUtil.getProperty("redis.ip2");
    private static Integer port2=Integer.parseInt(PropertiesUtil.getProperty("redis.port2"));
    static{
        initPool();
    }
    private static void initPool(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(testOnBorrow);
        config.setMinIdle(minIdle);
        config.setTestOnReturn(testOnReturn);
        JedisShardInfo info1=new JedisShardInfo(ip1,port1,2000*1);
        JedisShardInfo info2=new JedisShardInfo(ip2,port2,2000*1);
        List<JedisShardInfo> listInfo=new ArrayList<JedisShardInfo>();
        //MURMUR_HASH表示使用一致性hashing算法
        shardedJedisPool=new ShardedJedisPool(config,listInfo,Hashing.MURMUR_HASH);
    }

    public static ShardedJedis getJedisPool() {
        return shardedJedisPool.getResource();
    }
    public static void returnResource(ShardedJedis jedis){
        shardedJedisPool.returnResource(jedis);
    }
    public static void returnBrokenResource(ShardedJedis jedis){
        shardedJedisPool.returnBrokenResource(jedis);
    }
    //public static void main(String[] args){
     //   getJedisPool();
//    }
}
