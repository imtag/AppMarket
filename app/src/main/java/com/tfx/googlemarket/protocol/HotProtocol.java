package com.tfx.googlemarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfx.googlemarket.base.BaseProtocol;

public class HotProtocol extends BaseProtocol<List<String>> {

	@Override
	protected List<String> parseJsonString(String jsonString) {
		Gson gson = new Gson();
		//泛型解析  list类型
		return gson.fromJson(jsonString, new TypeToken<List<String>>() {
		}.getType());
	}

	@Override
	protected String getKeywords() {
		return "hot";
	}

}
