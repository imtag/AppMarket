package com.tfx.googlemarket.holder.detail;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.RatioLayout;

public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {

	@ViewInject(R.id.app_detail_pic_iv_container)
	LinearLayout	mLlPic;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_pic, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void refreshHolderView(AppInfoBean data) {
		List<String> datas = data.screen;
		for (int i = 0; i < datas.size(); i++) {
			String url = datas.get(i);

			ImageView iv = new ImageView(UIUtils.getContext());
			BitmapHelper.display(iv, URLS.IMAGEBASEURL + url);

			//比例布局 已知宽度求高度  屏幕刚好放下三张图片 不管分辨率多少
			RatioLayout rl = new RatioLayout(UIUtils.getContext());
			rl.setPicRatio((float) 150 / 250);
			rl.setRelative(RatioLayout.RELATIVE_WIDTH);
			rl.addView(iv);

			//获取屏幕宽度 设置图片的外层比例布局大小为屏幕三分之一
			int width = UIUtils.getResources().getDisplayMetrics().widthPixels - UIUtils.dp2px(UIUtils.getContext(), 13);
			LayoutParams params = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i != 0) {
				params.leftMargin = UIUtils.dp2px(UIUtils.getContext(), 3);
			}
			mLlPic.addView(rl, params);
		}
	}
}
