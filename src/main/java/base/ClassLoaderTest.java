package base;

import base.MyClassLoader;

/**
 * Created by jhui on 2017/5/26.
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println(ClassLoaderTest.class.getClassLoader());
        MyClassLoader loader = new MyClassLoader();
        Class c = loader.loadClass("jvm.ThreadPoolTest");
        System.out.println(c.getClassLoader());

    }
}
