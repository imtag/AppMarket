package com.tfx.googlemarket.protocol;

import com.google.gson.Gson;
import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.HomeBean;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-10
 * @desc      HomeFragment页面网络请求

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: HomeProtocol.java 4 2016-10-14 02:56:02Z tfx $
 */

public class HomeProtocol extends BaseProtocol<HomeBean> {
	//解析json
	@Override
	protected HomeBean parseJsonString(String jsonString) {
		Gson gson = new Gson();
		HomeBean homeBean = gson.fromJson(jsonString, HomeBean.class);
		return homeBean;
	}

	//返回具体关键字    baseurl后的
	@Override
	protected String getKeywords() {
		return "home";
	}
}
