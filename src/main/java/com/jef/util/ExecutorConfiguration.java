package com.jef.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jef
 * @date 2021/7/5
 */
@Configuration
@EnableAsync
public class ExecutorConfiguration {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    @Value("${thread.executor.maxPoolSize:50}")
    private Integer maxPoolSize;
    @Value("${thread.executor.queueCapacity:300}")
    private Integer queueCapacity;
//    @Bean("asyncServiceExecutor")
    public ThreadPoolTaskExecutor asyncServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(CPU_COUNT * 2 + 1);
        // executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(5);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        // AbortPolicy: 直接拒绝执行任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("aysnc_thread_");
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public ScheduledThreadPoolExecutor asyncScheduledThreadPoolExecutor(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        executor.setMaximumPoolSize(maxPoolSize);
        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        return executor;
    }

}