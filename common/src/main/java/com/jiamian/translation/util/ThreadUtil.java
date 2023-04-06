package com.jiamian.translation.util;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class ThreadUtil {

	private static int MAX_THREAD_POOL_SIZE = 100;
	private static AtomicLong THREAD_POOL_EXE_COUNT = new AtomicLong(0);

	private static ThreadPoolExecutor executorPool = new ThreadPoolExecutor(20,
			MAX_THREAD_POOL_SIZE, 60, TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static ThreadPoolExecutor getExecutorPool() {
		int runningNum = executorPool.getActiveCount();
		if (runningNum >= MAX_THREAD_POOL_SIZE) {
			LoggerUtil.info(
					"executorPool running thread is achieve the big num = {}, trig times = {}",
					runningNum, THREAD_POOL_EXE_COUNT.incrementAndGet());
		}
		return executorPool;
	}
}
