package base;

import java.io.*;

/**
 * Created by jhui on 2017/5/26.
 */
public class MyClassLoader extends java.lang.ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name,data,0,data.length);
    }


    public byte[] loadClassData(String name){
        InputStream in = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try {
            name = name.replace(".", "\\");
            in = new BufferedInputStream(new FileInputStream(new File("D:\\test\\" + name + ".class")));
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
