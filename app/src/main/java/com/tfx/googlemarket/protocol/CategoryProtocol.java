package com.tfx.googlemarket.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tfx.googlemarket.base.BaseProtocol;
import com.tfx.googlemarket.bean.CategoryInfoBean;

public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {

	@Override
	protected List<CategoryInfoBean> parseJsonString(String jsonString) {
		List<CategoryInfoBean> datas = new ArrayList<CategoryInfoBean>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				//封装标题bean
				CategoryInfoBean titleBean = new CategoryInfoBean();
				titleBean.isTitle = true;
				titleBean.title = jsonObject.getString("title");
				datas.add(titleBean);

				//封装详细类型bean
				JSONArray jsonArray2 = jsonObject.getJSONArray("infos");
				for (int j = 0; j < jsonArray2.length(); j++) {
					JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
					CategoryInfoBean detailBean = new CategoryInfoBean();
					detailBean.name1 = jsonObject2.getString("name1");
					detailBean.name2 = jsonObject2.getString("name2");
					detailBean.name3 = jsonObject2.getString("name3");
					detailBean.url1 = jsonObject2.getString("url1");
					detailBean.url2 = jsonObject2.getString("url2");
					detailBean.url3 = jsonObject2.getString("url3");
					datas.add(detailBean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	protected String getKeywords() {
		return "category";
	}

}
