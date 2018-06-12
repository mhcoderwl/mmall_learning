package com.mmall.common;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//google的guava缓存
import java.util.concurrent.TimeUnit;


public class TokenCache {
    public static final String TOKEN_PREFIX = "token_";
    public static Logger logger=LoggerFactory.getLogger(TokenCache.class);
    public static LoadingCache<String,String> cache=CacheBuilder.newBuilder().maximumSize(10000)
            .initialCapacity(1000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader() {
                       @Override
                       public Object load(Object o) throws Exception {
                           return null;
                       }
                   }
            );
    public static void setKey(String key,String value){
        cache.put(key,value);
    }
    public static String getKey(String key){
        String str=null;
        try{
            str=cache.get(key);
        }catch (Exception e){
            logger.error("cache has error",e);
        }
        finally {
            return str;
        }
    }

}
