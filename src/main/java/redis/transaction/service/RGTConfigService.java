package redis.transaction.service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.transaction.util.FileUtil;
import redis.transaction.util.GlobalConstants;
import redis.transaction.util.JdomUtils;

/**
 * Created by jiangwenping on 16/11/29.
 */
public class RGTConfigService {

    public JedisPoolConfig initRediPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        int maxIdle = 30;
        boolean testWhileIdle = true;
        int timeBetweenEvictionRunsMillis = 60000;
        int numTestsPerEvictionRun = 30;
        int minEvictableIdleTimeMillis = 60000;
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return jedisPoolConfig;
    }

    public JedisPool initRedis(JedisPoolConfig jedisPoolConfig) {
        String host = "127.0.0.1";
        int port = 6379;
        int database = 0;
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port);
        return jedisPool;
    }
}
