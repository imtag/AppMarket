package com.tfx.googlemarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.AppInfoBean;

public class GameProtocol extends BaseProtocol<List<AppInfoBean>> {
	@Override
	protected List<AppInfoBean> parseJsonString(String jsonString) {
		Gson gson = new Gson();
		//泛型解析  list类型
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>() {
		}.getType());
	}

	@Override
	protected String getKeywords() {
		return "game";
	}

}
