package com.tfx.googlemarket.base;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tfx.googlemarket.config.Constants;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.FileUtils;
import com.tfx.googlemarket.utils.IOUtils;
import com.tfx.googlemarket.utils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-10
 * @desc      网络请求数据的基类
 *			    对请求到的数据进行三级缓存 
	
 * @version   $Rev: 4 $
 * @param <T> 
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BaseProtocol.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class BaseProtocol<T> {
	private Map<String, String>	mExtraParams;

	/*
	 * 请求数据封装  index：请求参数，key为index的值
	 */
	public T loadData(int index) throws HttpException, IOException {
		/*--------------- 三级缓存 ----------------*/

		//1.内存获取
		T dataFromMemory = getDataFromMemory(index);
		if (dataFromMemory != null) {
			LogUtils.d(this.getClass().getSimpleName(), "内存加载数据" + getKeywords() + "." + index);
			return dataFromMemory;
		}

		//2.本地获取
		T dataFromLocal = getDataFromLocal(index);
		if (dataFromLocal != null) {
			LogUtils.d(this.getClass().getSimpleName(), "本地加载数据" + getKeywords() + "." + index);
			return dataFromLocal;
		}

		//3.网络获取
		T dataFromNet = getDataFromNet(index);
		LogUtils.d(this.getClass().getSimpleName(), "网络加载数据" + getKeywords() + "." + index);
		return dataFromNet;
	}

	/**
	 * @desc 内存读取数据
	 */
	private T getDataFromMemory(int index) {
		//1.获取缓存 对象
		Map<String, String> cacheMap = BaseApplication.getmCacheMap();
		//2.获取缓存的json数据
		String jsonStr = "";
		mExtraParams = getExtraParams();
		if (mExtraParams != null) {
			jsonStr = cacheMap.get(getKeywords() + "." + mExtraParams.get("packageName"));
		} else {
			jsonStr = cacheMap.get(getKeywords() + "." + index);
		}

		//3.解析json
		if (jsonStr != null) {
			return parseJsonString(jsonStr);
		}
		return null;
	}

	/**
	 * @desc 本地硬盘读取数据
	 */
	private T getDataFromLocal(int index) {
		try {
			//文件路径   外置存储卡   sdcard/android/data/包     
			//文件名保证唯一索引性   防止重复
			File cacheFile;
			mExtraParams = getExtraParams();
			if (mExtraParams != null) {
				cacheFile = new File(FileUtils.getDir("json"), getKeywords() + "." + mExtraParams.get("packageName"));
			} else {
				cacheFile = new File(FileUtils.getDir("json"), getKeywords() + "." + index);
			}
			if (cacheFile.exists()) {
				BufferedReader reader = null;
				try {
					//读取插入时间  文件第一行
					reader = new BufferedReader(new FileReader(cacheFile));
					String insertTimeStr = reader.readLine();
					long insertTime = Long.parseLong(insertTimeStr);
					//判断是否过期
					if (System.currentTimeMillis() - insertTime < Constants.PROTOCOLTIMEOUT) {
						//没过期  读取缓存内容
						String cacheJsonData = reader.readLine();
						//Json解析解析内容
						T t = parseJsonString(cacheJsonData);
						return t;
					}
				} finally {
					//关闭流
					IOUtils.close(reader);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @desc 网络请求数据
	 */
	private T getDataFromNet(int index) throws HttpException, IOException {
		//1.网络请求数据   http://localhost:8080/GooglePlayServer/home?index=0
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		String url = URLS.BASEURL + getKeywords();
		RequestParams params = new RequestParams(); //添加请求参数 ？号后面的 

		mExtraParams = getExtraParams();
		if (mExtraParams != null) {
			for (Map.Entry<String, String> info : mExtraParams.entrySet()) {
				String key = info.getKey();
				String value = info.getValue();
				params.addQueryStringParameter(key, value);
			}
		} else {
			params.addQueryStringParameter("index", index + ""); //index传入
		}

		ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
		String jsonString = responseStream.readString(); //流转string

		//1.3将jso数据保存到内存map对象
		Map<String, String> cacheMap = BaseApplication.getmCacheMap();
		if (mExtraParams != null) {
			//详细页面 协议  key为包名 保证唯一性
			cacheMap.put(getKeywords() + "." + mExtraParams.get("packageName"), jsonString);
		} else {
			cacheMap.put(getKeywords() + "." + index, jsonString);
		}

		//1.6将json数据保存到本地文件
		BufferedWriter writer = null;
		try {
			File cacheFile;
			if (mExtraParams != null) {
				//详细页面 协议  key为包名 保证唯一性
				cacheFile = new File(FileUtils.getDir("json"), getKeywords() + "." + mExtraParams.get("packageName")); //写入的文件
			} else {
				cacheFile = new File(FileUtils.getDir("json"), getKeywords() + "." + index); //写入的文件
			}
			writer = new BufferedWriter(new FileWriter(cacheFile));
			writer.write(System.currentTimeMillis() + ""); //写当前时间
			writer.newLine(); //换行
			writer.write(jsonString); //写json数据
		} finally {
			IOUtils.close(writer);
		}

		//2.解析json
		T t = parseJsonString(jsonString);
		return t;
	}

	//添加额外参数 子类实现
	protected Map<String, String> getExtraParams() {
		return null;
	}

	/**
	 * @desc 解析json数据  子类具体实现 
	 * @desc 返回的数据类型还不确定，可能是list或者bean...，使用泛型，不指定具体类型，子类具体返回
	 */
	protected abstract T parseJsonString(String jsonString);

	/**
	 * @desc 加载数据的关键字   home / app / game .....
	 * @desc 子类具体实现 
	 */
	protected abstract String getKeywords();
}
