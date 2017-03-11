package com.tfx.googlemarket.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.holder.detail.AppDetailDownloadHolder;
import com.tfx.googlemarket.manager.DownloadInfo;
import com.tfx.googlemarket.manager.DownloadManager;
import com.tfx.googlemarket.manager.DownloadManager.DownloadInfoObserver;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.StringUtils;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.CircleProgressView;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-9
 * @desc      app视图持有者，  提供视图，接收数据，数据和视图绑定

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: AppHolder.java 4 2016-10-14 02:56:02Z tfx $
 */

public class AppHolder extends BaseHolder<AppInfoBean> implements OnClickListener, DownloadInfoObserver {
	@ViewInject(R.id.item_appinfo_iv_icon)
	ImageView			mIvIcon;

	@ViewInject(R.id.item_appinfo_rb_stars)
	RatingBar			mRbStars;

	@ViewInject(R.id.item_appinfo_tv_des)
	TextView			mTvDes;

	@ViewInject(R.id.item_appinfo_tv_size)
	TextView			mTvSize;

	@ViewInject(R.id.item_appinfo_tv_title)
	TextView			mTvTitle;

	@ViewInject(R.id.item_appinfo_cpv_progress)
	CircleProgressView	mCpvDownload;

	private AppInfoBean	mDatas;

	/*
	 * 初始化根视图和子孩子
	 */
	@Override
	protected View initHolderView() {
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
		ViewUtils.inject(this, rootView);
		mCpvDownload.setOnClickListener(this);
		return rootView;
	}

	/* 
	 * 数据和视图绑定
	 */
	@Override
	protected void refreshHolderView(AppInfoBean data) {
		//清除复用convertVIew之后的progress效果
		mCpvDownload.setProgress(0);

		mDatas = data;
		mTvDes.setText(data.des);
		mTvSize.setText(StringUtils.formatFileSize(data.size));
		mTvTitle.setText(data.name);
		mRbStars.setRating(data.stars);

		//图片加载 http://localhost:8080/GooglePlayServer/image?name=app/com.itheima.www/icon.jpg
		BitmapHelper.display(mIvIcon, URLS.IMAGEBASEURL + data.iconUrl);

		getDownloadInfoToRefreshUi();
	}

	public void getDownloadInfoToRefreshUi() {
		//获取当前最新的downloadInfo，去刷新ui
		DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(mDatas);
		refreshCircleProgressView(downloadInfo);
	}

	private void refreshCircleProgressView(DownloadInfo info) {
		// 根据不同的状态给用户提示 处理ui  
		mCpvDownload.setEnableProgress(true);
		mCpvDownload.setMax(info.maxProgress);
		mCpvDownload.setProgress(info.curProgress);
		int progress = (int) (info.curProgress * 1.0f / info.maxProgress * 100);

		switch (info.state) {
		case DownloadManager.STATE_UNDOWNLOAD:
			//未下载
			mCpvDownload.setIcon(R.drawable.ic_download);
			mCpvDownload.setText("下载");
			break;
		case DownloadManager.STATE_DOWNLOADING:
			//下载中 
			mCpvDownload.setIcon(R.drawable.ic_pause);
			mCpvDownload.setText(progress + "%");
			break;
		case DownloadManager.STATE_PAUSEDOWNLOAD:
			//暂停下载
			mCpvDownload.setIcon(R.drawable.ic_resume);
			mCpvDownload.setText("继续下载");
			break;
		case DownloadManager.STATE_WAITINGDOWNLOAD:
			//等待下载
			mCpvDownload.setIcon(R.drawable.ic_pause);
			mCpvDownload.setText("等待中...");
			break;
		case DownloadManager.STATE_DOWNLOADFAILED:
			//下载失败
			mCpvDownload.setIcon(R.drawable.ic_redownload);
			mCpvDownload.setText("重试");
			break;
		case DownloadManager.STATE_DOWNLOADED:
			//下载完成
			mCpvDownload.setIcon(R.drawable.ic_install);
			mCpvDownload.setEnableProgress(false);
			mCpvDownload.setText("安装");
			break;
		case DownloadManager.STATE_INSTALLED:
			//已安装
			mCpvDownload.setIcon(R.drawable.ic_install);
			mCpvDownload.setText("打开");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_appinfo_cpv_progress:
			processClick();
			break;
		}
	}

	public void processClick() {
		AppDetailDownloadHolder appDetailDownloadHolder = new AppDetailDownloadHolder();

		//获取当前状态
		DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(mDatas);
		/* 处理不同的状态的点击事件  */
		switch (info.state) {
		case DownloadManager.STATE_UNDOWNLOAD:
			//未下载  点击下载
			appDetailDownloadHolder.doDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADING:
			//下载中 点击暂停下载
			appDetailDownloadHolder.pauseDownload(info);
			break;
		case DownloadManager.STATE_PAUSEDOWNLOAD:
			//暂停下载 点击继续下载
			appDetailDownloadHolder.doDownload(info);
			break;
		case DownloadManager.STATE_WAITINGDOWNLOAD:
			//等待下载 点击取消下载
			appDetailDownloadHolder.cancelDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADFAILED:
			//下载失败 点击重新
			appDetailDownloadHolder.doDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADED:
			//下载完成 安装
			appDetailDownloadHolder.installApp(info);
			break;
		case DownloadManager.STATE_INSTALLED:
			//已安装 打开 
			appDetailDownloadHolder.openApp(info);
			break;
		default:
			break;
		}
	}

	//DownloadInfo改变回调 刷新ui显示
	@Override
	public void onDownloadInfoChange(final DownloadInfo info) {
		//过滤掉不是当前应用的DownloadInfo
		if (!info.packageName.equals(mDatas.packageName)) {
			return;
		}
		//onDownloadInfoChange在下载的子线程中调用的  所以 更新ui要在主线程
		UIUtils.postTaskSafely(new Runnable() {
			@Override
			public void run() {
				//更新progressBt显示
				refreshCircleProgressView(info);
			}
		});
	}
}
