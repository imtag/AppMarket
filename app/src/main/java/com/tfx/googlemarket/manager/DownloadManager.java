package com.tfx.googlemarket.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.factory.ThreadPoolExecutorProxyFactory;
import com.tfx.googlemarket.utils.CommonUtils;
import com.tfx.googlemarket.utils.FileUtils;
import com.tfx.googlemarket.utils.IOUtils;
import com.tfx.googlemarket.utils.UIUtils;

/** 
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-18
 * @desc      下载管理器，需要时刻记录当前下载状态

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class DownloadManager {
	public static DownloadManager		instance;
	public static final int				STATE_UNDOWNLOAD		= 0;									// 未下载
	public static final int				STATE_DOWNLOADING		= 1;									// 下载中
	public static final int				STATE_PAUSEDOWNLOAD		= 2;									// 暂停下载
	public static final int				STATE_WAITINGDOWNLOAD	= 3;									// 等待下载
	public static final int				STATE_DOWNLOADFAILED	= 4;									// 下载失败
	public static final int				STATE_DOWNLOADED		= 5;									// 下载完成
	public static final int				STATE_INSTALLED			= 6;									// 已安装
	int									count					= 0;

	//记录正在下载的一些downloadInfo
	public Map<String, DownloadInfo>	mDownloadInfoMaps		= new HashMap<String, DownloadInfo>();

	//得到下载管理器实例，单例
	public static DownloadManager getInstance() {
		if (instance == null) {
			synchronized (DownloadManager.class) {
				if (instance == null) {
					instance = new DownloadManager();
				}
			}
		}
		return instance;
	}

	//用户点击了下载按钮  进入下载 
	public void download(DownloadInfo info) {
		//走到这里  当前app进入了下载   添加到记录正在下载的map 
		mDownloadInfoMaps.put(info.packageName, info);

		/*============================== 未下载  ==============================*/
		info.state = STATE_UNDOWNLOAD;
		notifyObservers(info); //状态改变 通知观察者

		/*============================== 等待状态 工作线程3个 第4个任务进入缓存队列  ==============================*/
		info.state = STATE_WAITINGDOWNLOAD;
		notifyObservers(info); //状态改变 通知观察者

		//线程池执行下载任务
		ThreadPoolExecutorProxyFactory.getDownloadThreadPoolExecutorProxy().execute(new DownloadTask(info));
	}

	//下载的任务
	class DownloadTask implements Runnable {
		DownloadInfo	mInfo;
		private boolean	isPause	= false;

		public DownloadTask(DownloadInfo info) {
			this.mInfo = info;
		}

		@Override
		public void run() {
			try {
				count++;

				//设置task给自己
				mInfo.task = this;

				/*============================== 下载中  ==============================*/
				mInfo.state = STATE_DOWNLOADING;
				notifyObservers(mInfo); //状态改变 通知观察者 

				//处理断点
				long initRange = 0;
				File apkFile = new File(mInfo.savePath, mInfo.saveName);
				if (apkFile.exists()) {
					initRange = apkFile.length(); //未下载完成的apk已经下载的长度
				}
				mInfo.curProgress = initRange;

				//http://localhost:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("name", mInfo.downloadUrl);
				params.addQueryStringParameter("range", initRange + ""); //断点
				ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, URLS.DOWNLOADURL, params);
				if (responseStream.getStatusCode() == 200) {
					//请求成功 将数据保存到缓存目录
					InputStream is = null;
					FileOutputStream fos = null;
					try {
						//请求到的输入流 
						is = responseStream.getBaseStream();
						//输出流
						fos = new FileOutputStream(apkFile, true);//允许追加

						//文件读写
						byte[] buffer = new byte[1024];
						int len;
						while ((len = is.read(buffer)) != -1) {
							//暂停下载
							if (mInfo.state == STATE_PAUSEDOWNLOAD) {
								isPause = true;
								break;
							}
							fos.write(buffer, 0, len);
							mInfo.curProgress += len;

							/*============================== 下载中  ==============================*/
							mInfo.state = STATE_DOWNLOADING;
							notifyObservers(mInfo); //状态改变 通知观察者

							//下载完了
							if (mInfo.curProgress == mInfo.maxProgress) {
								isPause = false;
								break;
							}
						}
						if (isPause) {
							//用户暂停了下载 
							mInfo.state = STATE_PAUSEDOWNLOAD;
							notifyObservers(mInfo);
						} else {
							/*============================== 下载完成  ==============================*/
							mInfo.state = STATE_DOWNLOADED;
							notifyObservers(mInfo); //状态改变 通知观察者
						}
					} finally {
						IOUtils.close(fos);
						IOUtils.close(is);
					}
				} else {
					//非200 请求失败
					/*============================== 下载失败  ==============================*/
					mInfo.state = STATE_DOWNLOADFAILED;
					notifyObservers(mInfo); //状态改变 通知观察者 
				}
			} catch (Exception e) {
				e.printStackTrace();
				/*============================== 下载失败  ==============================*/
				mInfo.state = STATE_DOWNLOADFAILED;
				notifyObservers(mInfo); //状态改变 通知观察者
			}
		}
	}

	/**
	 * @desc 暴露当前状态，也就是需要提供downloadInfo
	 * @call 外界需要知道最新的state
	 */
	public DownloadInfo getDownloadInfo(AppInfoBean data) {
		//apk文件
		File mApkFile = new File(FileUtils.getDir("download"), data.packageName + ".apk");

		//DownloadInfo
		DownloadInfo info = new DownloadInfo();
		info.downloadUrl = data.downloadUrl;
		info.saveName = data.packageName + ".apk";
		info.savePath = FileUtils.getDir("download");
		info.packageName = data.packageName;
		info.curProgress = 0;
		info.maxProgress = data.size;

		//已安装
		if (CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
			info.state = DownloadManager.STATE_INSTALLED;
			return info;
		}

		//下载完成
		if (mApkFile.exists()) {
			if (mApkFile.length() == data.size) {
				info.state = DownloadManager.STATE_DOWNLOADED;
				return info;
			}
		}

		//下载中 、暂停下载、 等待下载、 下载失败
		DownloadInfo downloadInfo = mDownloadInfoMaps.get(data.packageName);//正在下载的map中能找到，则是四个状态中一个
		if (downloadInfo != null) {
			return downloadInfo;
		}

		//走到这里   未下载
		info.state = STATE_UNDOWNLOAD;
		return info;
	}

	/*============================== 自定义观察者 观察DownloadInfo改变 ==============================*/
	public interface DownloadInfoObserver {
		//DownloadInfo改变回调
		void onDownloadInfoChange(DownloadInfo info);
	}

	//观察者集合
	List<DownloadInfoObserver>	downloadInfoObservers	= new ArrayList<DownloadInfoObserver>();

	/**添加观察者*/
	public void addObserver(DownloadInfoObserver observer) {
		if (observer == null) {
			throw new NullPointerException("observer == null");
		}
		synchronized (this) {
			if (!downloadInfoObservers.contains(observer))
				//把观察者添加到观察者集合
				downloadInfoObservers.add(observer);
		}
	}

	/**删除观察者*/
	public synchronized void deleteObserver(DownloadInfoObserver observer) {
		//把观察者从集合中删除
		downloadInfoObservers.remove(observer);
	}

	/**通知观察者数据改变  把改变的downloadInfo传过来*/
	public void notifyObservers(DownloadInfo info) {
		for (DownloadInfoObserver observer : downloadInfoObservers) {
			//数据改变  遍历所有观察者  更新
			observer.onDownloadInfoChange(info);
		}
	}

	/*============================== 自定义观察者结束 ==============================*/

	//暂停下载
	public void pause(DownloadInfo downloadInfo) {
		downloadInfo.state = STATE_PAUSEDOWNLOAD;
		notifyObservers(downloadInfo);
	}

	//取消下载
	public void cancel(DownloadInfo info) {
		//找到线程池,移除当前任务
		ThreadPoolExecutorProxyFactory.getDownloadThreadPoolExecutorProxy().remove(info.task);

		//更改状态 通知观察者
		info.state = STATE_UNDOWNLOAD;
		notifyObservers(info);
	}
}
