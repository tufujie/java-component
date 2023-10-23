package com.jef.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jef
 * @date 2018/12/14 17:38
 */
public class ThreadUtil {
    private static ThreadUtil threadUtil = null;

    static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue<Runnable>(50);
    private ThreadPoolExecutor threadPoolExecutor;
    private ThreadPoolTaskExecutor asyncServiceExecutor;
    private static ThreadPoolExecutor threadPoolExecutorV2;

    ThreadUtil(String targetName) {
        asyncServiceExecutor = createExecutor(50, 200, 50, targetName);
        threadPoolExecutor = asyncServiceExecutor.getThreadPoolExecutor();
    }

    /**
     * 获取系统线程组
     *
     * @return
     */
    public static ThreadGroup getSystemThreadGroup() {
        ThreadGroup systemThreadGroup;
        ThreadGroup parentThreadGroup;
        systemThreadGroup = Thread.currentThread().getThreadGroup();
        while ((parentThreadGroup = systemThreadGroup.getParent()) != null) {
            systemThreadGroup = parentThreadGroup;
        }
        return systemThreadGroup;
    }

    public static void printSystemThreadGroup() {
        System.out.println(getSystemThreadGroup());
    }

    public static ThreadGroup[] getThreadGroup() {
        ThreadGroup stg = getSystemThreadGroup();
        int nog = stg.activeGroupCount()+1;
        ThreadGroup[] tga = new ThreadGroup[nog];
        stg.enumerate(tga);
        tga[tga.length - 1] = stg;
        return tga;
    }


    /**
     * 打印线程数据
     *
     * @param o
     */
    public static void printThreadName(Thread[] o) {
        System.out.println("------开始输出当前的所有线程------");
        for (Thread x : o) {
            System.out.println(x + " isAlive=" + x.isAlive() + " 线程名称=" + x.getName() + " isDaemon=" + x.isDaemon());
        }
        System.out.println("------输出当前的所有线程结束------");
    }

    /**
     * 打印当前线程名称
     */
    public static void printThreadName() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】");
    }

    /**
     * 打印当前线程优先级
     */
    public static void printCurrentThreadPriorty() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】线程优先级=【" + Thread.currentThread().getPriority() + "】");
    }

    /**
     * 打印当前线程是否后台进程
     */
    public static void printCurrentThreadIsDaemon() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】是否后台进程=【" + (Thread.currentThread().isDaemon() ? "是" : "否") + "】");
    }

    /**
     * 打印当前线程是否中断
     */
    public static void printCurrentThreadIsInterrupted() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】是否中断=【" + (Thread.currentThread().isInterrupted() ? "是" : "否") + "】");
    }

    /**
     * 打印当前线程的interrupted()方法是否被调用过，用来检查中断状态。而且可以清除中断状态
     */
    public static void printCurrentThreadInterrupted() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】interrupted()方法是否被调用过=【" + (Thread.interrupted() ? "是" : "否") + "】");
    }

    /**
     * 打印当前线程是否中断
     */
    public static void printCurrentThreadIsAlive() {
        System.out.println("当前线程名称=【" + Thread.currentThread().getName() + "】是否存活=【" + (Thread.currentThread().isAlive() ? "是" : "否") + "】");
    }

    /**
     * 打印当前线程是否中断
     */
    public static void printThreadInfo(Thread thread) {
        System.out.println("当前线程名称=【" + thread.getName() + "】是否存活=【" + (thread.isAlive() ? "是" : "否") + "】是否后台进程=【"
                + (thread.isDaemon() ? "是" : "否") + "】是否中断=【" + (thread.isInterrupted() ? "是" : "否") + "】");
    }


    /**
     * 获取当前线程名称
     */
    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取所有的线程
     *
     * @return
     */
    public static Thread[] getThreads() {
        ThreadGroup stg = getSystemThreadGroup();
        Thread[] ta = new Thread[stg.activeCount()];
        stg.enumerate(ta, true);
        return ta;
    }

    public static void setPriority(Thread[] ta, int p) {
        for(Thread x:ta) {
            x.setPriority(p);
        }
    }

    /**
     * 修改线程名称
     * @param ta
     * @param n
     */
    public static void setName(Thread[] ta, String n) {
        for (Thread x : ta) {
            x.setName(n);
        }
    }

    /**
     * 执行线程
     * @param threadName
     * @param runnable
     */
    public static void executeThread(String threadName, Runnable runnable) {
        ThreadPoolExecutor pool = initThreadPool(threadName);
        pool.execute(runnable);
        pool.shutdown();
    }

    /**
     * 初始化线程
     *
     * @param targetName
     * @return
     */
    private static ThreadPoolExecutor initThreadPool(String targetName) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(targetName + "-pool-%d").build();
        return new ThreadPoolExecutor(10, 50,
                20L, TimeUnit.MILLISECONDS, linkedBlockingQueue, namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 执行线程
     * @param threadName
     * @param runnable
     */
    public static void executeThreadTwo(String threadName, Runnable runnable) {
        ScheduledExecutorService pool = initThreadPoolTwo(threadName);
        pool.execute(runnable);
        pool.shutdown();
    }

    /**
     * 初始化线程方式2
     *
     * @param targetName
     * @return
     */
    private static ScheduledExecutorService initThreadPoolTwo(String targetName) {
        return new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(targetName + "-pool-%d").daemon(true).build());
    }

    public static ThreadUtil getInstance(String targetName) {
        if (threadUtil == null) {
            threadUtil = new ThreadUtil(targetName);
        }
        return threadUtil;
    }

    public static ThreadPoolTaskExecutor createExecutor(int maxPoolSize, int keepAliveSeconds, int queueCapacity, String targetName) {
        System.out.println("create threadPoolExecutor......");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1);
        // executor.setCorePoolSize(10);
        // 配置最大线程数，一定要大于等于自己创建的队列数量
        executor.setMaxPoolSize(maxPoolSize);
        // 线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 配置队列大小，>=mainExecutor.maxSize
        executor.setQueueCapacity(queueCapacity);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        // AbortPolicy: 直接拒绝执行任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(targetName + "-pool-%d").build();
        executor.setThreadFactory(namedThreadFactory);
        // 线程名字前缀
        executor.setThreadNamePrefix(targetName);
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * spring-boot web项目线程池
     *
     * @param task 任务线程
     * @return void
     * @throws
     * @date 2019/6/28 15:42
     */
    public void execute(Runnable task, String model) {
        execute(asyncServiceExecutor, task, model);
    }

    public void execute(ThreadPoolTaskExecutor executor, Runnable task, String model) {
        showThreadPoolInfo(model);
        executor.execute(task);
    }

    /**
     * spring-boot web项目线程池
     *
     * @param tasks 任务列表
     * @return void
     * @throws
     * @date 2019/6/28 15:42
     */
    public void execute(List<? extends Runnable> tasks, String model) {
        execute(asyncServiceExecutor, tasks, model);
    }

    public void execute(ThreadPoolTaskExecutor executor, List<? extends Runnable> tasks, String model) {
        showThreadPoolInfo(model);
        for (Runnable task : tasks) {
            executor.execute(task);
        }
    }

    /**
     * 注意：传入的task对象的run方法需要处理异常（try-catch），否则submit会吞掉异常
     *
     * @param task
     * @param model
     * @return java.util.concurrent.Future<?>
     * @throws
     * @date 2019/7/15 9:49
     */
    public Future<?> submit(Runnable task, String model) {
        return submit(asyncServiceExecutor, task, model);
    }

    public Future<?> submit(ThreadPoolTaskExecutor executor, Runnable task, String model) {
        showThreadPoolInfo(model);
        Future<?> submit = executor.submit(task);
        return submit;
    }

    /**
     * 有返回值的多线程处理
     * 注意：传入的task对象的run方法需要处理异常（try-catch），否则submit会吞掉异常
     *
     * @param task
     * @param model
     * @return java.util.concurrent.Future<?>
     * @throws
     * @date 2019/7/15 9:51
     */
    public <T> Future<T> submit(Callable<T> task, String model) {
        return submit(asyncServiceExecutor, task, model);
    }

    public <T> Future<T> submit(ThreadPoolTaskExecutor executor, Callable<T> task, String model) {
        showThreadPoolInfo(model);
        Future<T> submit = executor.submit(task);
        return submit;
    }

    /**
     * 显示线程池信息
     *
     * @param mode
     */
    private void showThreadPoolInfo(String mode) {
        if (null == threadPoolExecutor) {
            return;
        }
        /*System.out.printf("currentThread:{},threadPoolInfo:corePoolSize[{}],poolSize[{}],largestPoolSize[{}],taskCount[{}],completedTaskCount[{}],activeCount[{}],queueSize[{}]",
                mode,
                threadPoolExecutor.getCorePoolSize(),
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getLargestPoolSize(),
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getQueue().size());*/
    }

}
