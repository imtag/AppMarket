package com.tfx.googlemarket.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-6
 * @desc      全局 单例的盒子 大家都可以用

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BaseApplication.java 4 2016-10-14 02:56:02Z tfx $
 */

public class BaseApplication extends Application {
	private static Context				mContext;
	private static Handler				mHandler;
	private static int					mMainThreadId;

	//对象 用来内存缓存
	private static Map<String, String>	mCacheMap	= new HashMap<String, String>(); //map保证唯一索引性

	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Map<String, String> getmCacheMap() {
		return mCacheMap;
	}

	@Override
	public void onCreate() {
		//程序入口 

		//1.上下文
		mContext = getApplicationContext();

		//2.主线程handler
		mHandler = new Handler();

		//3.获取调用进程的线程id
		mMainThreadId = Process.myTid();

		super.onCreate();
	}
}
