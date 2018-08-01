package redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;

public class RedisClient {
    ClassPathXmlApplicationContext context = null;

    public static void main(String[] args) {
        RedisClient client = new RedisClient();
        client.init();
        client.crud();
    }

    public void crud(){
        Jedis jedis = getJedis();
        for (int i = 0; i < 10000; i++) {
            jedis.set("name" + i,"zhangsan" + i);
        }
    }

    public void get(String key){
        Jedis jedis = getJedis();
        System.out.println(jedis.get(key));
    }

    public Jedis getJedis(){
        JedisPool jedisPool = (JedisPool) context.getBean("jedisPool");
        return jedisPool.getResource();
    }

    public void init(){
        context = new ClassPathXmlApplicationContext(
                new String[] { "applicationContext-redis.xml" });
        context.start();
    }
}
