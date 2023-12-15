package com.jef;

import com.jef.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author tufujie
 * @date 2023/12/15
 */
public class DateTimeUtilTest {

    //执行总次数
    private static final int EXECUTE_COUNT = 1000;
    //同时运行的线程数量
    private static final int THREAD_COUNT = 20;

    private static ThreadLocal<DateFormat> THREAD_LOCAL = new ThreadLocal<DateFormat>();

    @Test
    public void testUseDateTimeFormatter() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
        ExecutorService executorService = Executors.newCachedThreadPool();
        String text = "2020-01-01";
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    try {
                        // 方式1，new SimpleDateFormat
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeUtil.NORMAL_DATE_FORMAT);
                        simpleDateFormat.parse(text);
                        // 方式2，DateTimeFormatter，推荐
                        LocalDate.parse(text, DateTimeUtil.FORMATTER);
                        // 方式3，org.joda.time.DateTime，推荐
                        DateTime.parse(text, DateTimeUtil.FORMATTER_2).toDate();
                        // 方式4，ThreadLocal
                        DateFormat dateFormat = THREAD_LOCAL.get();
                        if (dateFormat == null) {
                            dateFormat = new SimpleDateFormat(DateTimeUtil.NORMAL_DATE_FORMAT);
                            THREAD_LOCAL.set(dateFormat);
                        }
                        dateFormat.parse(text);
                        // 其他，方式5，synchronized，方式6，Lock，不建议
                    } catch (Exception e) {
                        System.out.println("线程：" + Thread.currentThread().getName() + " 格式化日期失败");
                        e.printStackTrace();
                        System.exit(1);
                    }
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println("信号量发生错误");
                    e.printStackTrace();
                    System.exit(1);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("所有线程格式化日期成功");
    }

}