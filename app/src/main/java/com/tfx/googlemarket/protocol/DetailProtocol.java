package com.tfx.googlemarket.protocol;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.AppInfoBean;

public class DetailProtocol extends BaseProtocol<AppInfoBean> {

	private String	mPackageName;

	public DetailProtocol(String packageName) {
		this.mPackageName = packageName;
	}

	@Override
	protected AppInfoBean parseJsonString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, AppInfoBean.class);
	}

	@Override
	protected String getKeywords() {
		return "detail";
	}

	@Override
	protected Map<String, String> getExtraParams() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("packageName", mPackageName);
		return map;
	}
}
