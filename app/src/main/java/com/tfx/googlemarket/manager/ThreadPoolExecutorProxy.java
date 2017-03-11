package com.tfx.googlemarket.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-8
 * @desc      线程池代理， 帮ThreadPoolExecutor做一些事 使用ThreadPoolExecutor更加方便，关心需要关心的即可
 * 			    提交任务，执行任务，移除任务
 * 
 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: ThreadPoolExecutorProxy.java 4 2016-10-14 02:56:02Z tfx $
 */

public class ThreadPoolExecutorProxy {
	ThreadPoolExecutor	mExecutor;
	private int			mCorePoolSize;
	private int			mMaximumPoolSize;
	private long		mKeepAliveTime;

	public ThreadPoolExecutorProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	/**
	 * @desc 初始化ThreadPoolExecutor线程池实例
	 * 
	 * 单例模式（双重检查加锁）
	 * 只有在第一次初始化的时候才启用同步机制，提高了性能
	 */
	private void initThreadPoolExecutor() {
		if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
			synchronized (ThreadPoolExecutorProxy.this) {
				if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
					TimeUnit unit = TimeUnit.MILLISECONDS; //毫秒
					BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(); //无限队列
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
					mExecutor = new ThreadPoolExecutor(mCorePoolSize, //核心线程数
							mMaximumPoolSize, //最大线程数
							mKeepAliveTime, //保持时间
							unit, //时间单位
							workQueue, //工作队列
							threadFactory, //线程工厂
							handler //异常捕获器
					);
				}
			}
		}
	}

	/*
	 * 单例：
	 * 		一个类只有一个实例
	 * 		一个类里面的成员变量只有一个实例啊
	 * 
	 * 提交任务和执行任务的区别
	 * 		1.是否有返回值
	 * 		2.Feture可以通过里面定义的get方法得到执行的结构
	 * 		3.Feture可以通过里面定义的get方法还可以捕获到执行过程中的异常信息
	 */
	//提交任务
	public Future<?> submit(Runnable task) {
		initThreadPoolExecutor();
		return mExecutor.submit(task);
	}

	//执行任务
	public void execute(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	//移除任务
	public void remove(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}
}
