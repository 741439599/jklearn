package jvm02;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * GCEasy   GCViewer   fastThread
 *
 * java -cp . -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:jvm02/gc.demo.log  -Xmx1g -Xms1g  jvm02.GCLogAnalysis
 *
 * java -cp . -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:jvm02/gc.demo.log  -XX:+UseSerialGC -Xmx1g -Xms1g  jvm02.GCLogAnalysis
 *
 * java -cp . -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:jvm02/gc.demo.log  -XX:+UseConcMarkSweepGC -Xmx1g -Xms1g  jvm02.GCLogAnalysis
 *
 * java -cp . -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:jvm02/gc.demo.log  -XX:+UseG1GC -Xmx1g -Xms1g  jvm02.GCLogAnalysis
 *-XX:+PrintGCDateStamps 打印时间戳
 * -Xloggc:jvm02/gc.demo.log 打印GC日志到指定文件
 *
 */
public class GCLogAnalysis {
    private static Random random = new Random();

    public static void main(String[] args) {
        //当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        //持续运行毫秒数；可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        //结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行。。。");

        //缓存一部分对象
        int cacheSize = 2000;
        Object[] cachedGarbage = new Object[cacheSize];
        //在此时间范围内、持续循环
        while (System.currentTimeMillis() < endMillis) {
            Object garbage = generateGarbage(100 * 1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cachedGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束！共生产对象次数：" + counter.longValue());
    }


    private static Object generateGarbage(int max) {
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result;
        switch (type){
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize){
                    builder.append(randomString);
                    builder.append(max);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
