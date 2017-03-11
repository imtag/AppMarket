package com.tfx.googlemarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.AppInfoBean;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-10
 * @desc      HomeFragment页面网络请求

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: ApplyProtocol.java 4 2016-10-14 02:56:02Z tfx $
 */

public class ApplyProtocol extends BaseProtocol<List<AppInfoBean>>  {
	//解析json
	@Override
	protected List<AppInfoBean> parseJsonString(String jsonString) {
		Gson gson = new Gson();
		//泛型解析  list类型
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>() {}.getType());
	}

	//返回具体关键字    baseurl后的
	@Override
	protected String getKeywords() {
		return "app";
	}
}
