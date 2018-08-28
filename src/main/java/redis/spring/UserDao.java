package redis.spring;

import com.google.common.collect.Lists;
import redis.User;

import java.util.List;

public class UserDao {
    public User selectByPrimaryKey(int userId){
        return new User(1,"xiaoming","nan",18);
    }

    public List<User> selectAllUser(){
        System.out.println("查询所有用户");
        List<User> list = Lists.newArrayList();
        list.add(new User(2,"xiaohong","nv",20));
        return list;
    }

    public void insertUser(User user){
        System.out.println("add user");
    }

    public void deleteUser(int id){
        System.out.println("delete user");
    }

    public List<User> findUsers(String keyWords){
        List<User> list = Lists.newArrayList();
        list.add(new User(2,"xiaohong","nv",20));
        return list;
    }

    public void editUser(User user){
        System.out.println("edit user");
    }
}
