package redis.spring;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.User;

import java.util.List;

public class RedisSpringClient {
    ClassPathXmlApplicationContext context = null;

    public static void main(String[] args) {
        RedisSpringClient client = new RedisSpringClient();
        client.init2();
        client.redisToService();
    }

    public void redisToService(){
        UserServiceImpl service = context.getBean(UserServiceImpl.class);
        List<User> list = service.getAllUser();
        System.out.println(JSONObject.toJSONString(list));
        List<User> list2 = service.getAllUser();
        System.out.println(JSONObject.toJSONString(list2));
        service.insertUser(new User());
        List<User> list3 = service.getAllUser();
        System.out.println(JSONObject.toJSONString(list3));
    }

    public void init2(){
        context = new ClassPathXmlApplicationContext(
                new String[] { "applicationContext-redis-templete.xml" });
        context.start();
    }
}
