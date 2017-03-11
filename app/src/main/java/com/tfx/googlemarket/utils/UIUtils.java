package com.tfx.googlemarket.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.tfx.googlemarket.base.BaseApplication;

/**
 * @创建者	 Administrator
 * @创时间 	 2015-10-20 上午10:16:38
 * @描述	     和ui相关的类
 *
 * @版本       $Rev: 4 $
 * @更新者     $Author: tfx $
 * @更新时间    $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @更新描述    TODO
 */
public class UIUtils {
	/**得到上下文*/
	public static Context getContext() {
		return BaseApplication.getContext();
	}

	/**得到Resource对象*/
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**得到string.xml中的字符*/
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**得到string.xml中的字符 */
	public static String getString(int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	/**得到string.xml中的字符数组*/
	public static String[] getStringArr(int resId) {
		return getResources().getStringArray(resId);
	}

	/**得到color.xml中的颜色值*/
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**得到应用程序的包名*/
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**得到主线程的Id*/
	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**得到主线程的hanlder*/
	public static Handler getMainThreadHandler() {
		return BaseApplication.getHandler();
	}

	/**
	 * 安全的执行一个task
	 * @return 
	 */
	public static void postTaskSafely(Runnable task) {
		//获得当前线程id
		int curThreadId = android.os.Process.myTid();
		//主线程id
		long mainThreadId = getMainThreadId();
		if (curThreadId == mainThreadId) {
			// 当前线程==主线程,直接执行任务
			task.run();
		} else {
			// 当前线程==子线程,通过消息机制,把任务交给主线程的Handler去执行
			getMainThreadHandler().post(task);
		}
	}

	/**
	* dp转px
	*
	* @param context 上下文
	* @param dpValue dp值
	* @return px值
	*/
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
