package com.tfx.googlemarket.holder.detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.StringUtils;
import com.tfx.googlemarket.utils.UIUtils;

public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {

	@ViewInject(R.id.app_detail_info_iv_icon)
	ImageView	mIvIcon;

	@ViewInject(R.id.app_detail_info_tv_downloadnum)
	TextView	mTvDownloadNum;

	@ViewInject(R.id.app_detail_info_tv_version)
	TextView	mTvVersion;

	@ViewInject(R.id.app_detail_info_tv_time)
	TextView	mTvTime;

	@ViewInject(R.id.app_detail_info_tv_size)
	TextView	mTvSize;

	@ViewInject(R.id.app_detail_info_tv_name)
	TextView	mTvName;

	@ViewInject(R.id.app_detail_info_rb_star)
	RatingBar	mRbStar;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void refreshHolderView(AppInfoBean data) {
		mTvDownloadNum.setText(UIUtils.getString(R.string.detail_downloadNum, data.downloadNum));
		mTvSize.setText(UIUtils.getString(R.string.detail_size, StringUtils.formatFileSize(data.size)));
		mTvTime.setText(UIUtils.getString(R.string.detail_date, data.date));
		mTvVersion.setText(UIUtils.getString(R.string.detail_version, data.version));
		mTvName.setText(data.name);
		mRbStar.setRating(data.stars);
		BitmapHelper.display(mIvIcon, URLS.IMAGEBASEURL + data.iconUrl);
	}

}
