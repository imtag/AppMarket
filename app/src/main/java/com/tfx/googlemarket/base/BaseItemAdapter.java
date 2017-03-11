package com.tfx.googlemarket.base;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.tfx.googlemarket.activity.DetailActivity;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.holder.AppHolder;
import com.tfx.googlemarket.manager.DownloadManager;
import com.tfx.googlemarket.utils.UIUtils;

public class BaseItemAdapter extends SuperBaseAdapter<AppInfoBean> {

	public BaseItemAdapter(AbsListView absListView, List<AppInfoBean> datas) {
		super(absListView, datas);
	}

	//返回一个BaseHolder的子类   当getView方法中convertView == null 初始化指定的根视图
	@Override
	protected BaseHolder<AppInfoBean> getSpecialHolder(int position) {
		AppHolder appHolder = new AppHolder();
		//添加appHolder为观察者
		DownloadManager.getInstance().addObserver(appHolder);
		return appHolder;
	}

	/**
	 * @desc 普通条目单击事件 
	 */
	@Override
	protected void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onNormalItemClick(parent, view, position, id);
		Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
		//把包名传递给详细页
		intent.putExtra("packageName", super.mDatas.get(position).packageName);
		//非activity类型context需要添加flag
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		UIUtils.getContext().startActivity(intent);
	}
}
