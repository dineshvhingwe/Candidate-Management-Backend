package com.sndcorp.candidatemanage;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer {
	@Bean("candidateManageUpdateExecutor")
	public ThreadPoolTaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(10);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("candidate-manage-thread-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		// Use custom cross thread request level thread factory classes
		int awaitTerminationSeconds = 5;
		executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
		executor.initialize();
		return executor;
	}

	@Override
	public Executor getAsyncExecutor() {
		return executor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> log.error("unknown exception occurred in thread pool execution task.", ex);
	}
}