package com.tfx.googlemarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.SubjectInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.UIUtils;

public class SubjectHolder extends BaseHolder<SubjectInfoBean> {

	private ImageView	mIvIcon;
	private TextView	mTvTitle;

	//初始化视图
	@Override
	protected View initHolderView() {
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
		mIvIcon = (ImageView) rootView.findViewById(R.id.item_subject_iv_icon);
		mTvTitle = (TextView) rootView.findViewById(R.id.item_subject_tv_title);
		return rootView;
	}

	//数据和视图绑定
	@Override
	protected void refreshHolderView(SubjectInfoBean data) {
		mTvTitle.setText(data.des);
		BitmapHelper.display(mIvIcon, URLS.IMAGEBASEURL + data.url);
	}

}
