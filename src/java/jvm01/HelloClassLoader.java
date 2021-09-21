package jvm01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，
 * 此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供
 */

public class HelloClassLoader extends ClassLoader {

    private final int BUFFER_SIZE = 100;

    public static void main(String[] args) throws Exception {
        Class<?> clazz = new HelloClassLoader().loadClass("Hello");
        Method hello = clazz.getDeclaredMethod("hello");
        hello.invoke(clazz.newInstance());
    }


    protected Class<?> findClass(String className) {
        InputStream in = HelloClassLoader.class.getClassLoader().getResourceAsStream("Hello.xlass");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[BUFFER_SIZE];
        int total;
        try {
            assert in != null;
            while ((total = in.read(buff, 0, BUFFER_SIZE)) > 0) {
                out.write(buff, 0, total);
            }
            byte[] bytearray = out.toByteArray();
            byte[] bytes = new byte[bytearray.length];
            for (int i = 0; i < bytearray.length; i++) {
                bytes[i] = (byte) ((255 -bytearray[i]));
            }

            String path = System.getProperty("user.dir");
            System.out.println("系统路径 ：" + path);
            File file = new File(path + "/src/java/jvm01/Hello.class");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream os = new FileOutputStream(file, false);
            os.write(bytes);
            os.close();
            return defineClass(className, bytes, 0, bytes.length);
            //  return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
