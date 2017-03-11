package com.tfx.googlemarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfx.googlemarket.base.BaseProtocol;

public class RecommendProtocol extends BaseProtocol<List<String>> {

	@Override
	protected List<String> parseJsonString(String jsonString) {
		return new Gson().fromJson(jsonString, new TypeToken<List<String>>() {
		}.getType());
	}

	@Override
	protected String getKeywords() {
		return "recommend";
	}

}
