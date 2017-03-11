package com.tfx.googlemarket.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.SubjectInfoBean;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>> {

	@Override
	protected List<SubjectInfoBean> parseJsonString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<SubjectInfoBean>>() {
		}.getType());
	}

	@Override
	protected String getKeywords() {
		return "subject";
	}

}
