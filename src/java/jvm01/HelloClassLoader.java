package jvm01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * �Զ���һ�� Classloader������һ�� Hello.xlass �ļ���ִ�� hello ������
 * ���ļ�������һ�� Hello.class �ļ������ֽڣ�x=255-x���������ļ����ļ�Ⱥ���ṩ
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
            System.out.println("ϵͳ·�� ��" + path);
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
