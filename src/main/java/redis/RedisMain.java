package redis;

import redis.sharded.ShardedRedisUtil;

public class RedisMain {
    public static void main(String[] args) {
        ShardedRedisUtil shardedRedis = ShardedRedisUtil.getInstance();
        shardedRedis.set("cnblog", "cnblog");
        shardedRedis.set("redis", "redis");
        shardedRedis.set("test", "test");
        shardedRedis.set("123456", "1234567");
    }
}
