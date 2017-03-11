package com.tfx.googlemarket.factory;

import com.tfx.googlemarket.manager.ThreadPoolExecutorProxy;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-8
 * @desc      线程池代理的工厂

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: ThreadPoolExecutorProxyFactory.java 4 2016-10-14 02:56:02Z tfx $
 */

public class ThreadPoolExecutorProxyFactory {
	
	static ThreadPoolExecutorProxy	mNormalThreadPoolExecutorProxy;
	static ThreadPoolExecutorProxy	mDownloadThreadPoolExecutorProxy;
	
	//得到普通线程池代理
	public static ThreadPoolExecutorProxy getNormalThreadPoolExecutorProxy(){
		if(mNormalThreadPoolExecutorProxy == null){
			synchronized (ThreadPoolExecutorProxyFactory.class) {
				if(mNormalThreadPoolExecutorProxy == null){
					mNormalThreadPoolExecutorProxy = new ThreadPoolExecutorProxy(5, 5, 3000);
				}
			}
		}
		return mNormalThreadPoolExecutorProxy;
	}
	
	//得到下载线程池代理
	public static ThreadPoolExecutorProxy getDownloadThreadPoolExecutorProxy(){
		if(mDownloadThreadPoolExecutorProxy == null){
			synchronized (ThreadPoolExecutorProxyFactory.class) {
				if(mDownloadThreadPoolExecutorProxy == null){
					mDownloadThreadPoolExecutorProxy = new ThreadPoolExecutorProxy(3, 3, 3000);
				}
			}
		}
		return mDownloadThreadPoolExecutorProxy;
	}
}
