package com.tfx.googlemarket.holder.detail;

import java.io.File;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.manager.DownloadInfo;
import com.tfx.googlemarket.manager.DownloadManager;
import com.tfx.googlemarket.manager.DownloadManager.DownloadInfoObserver;
import com.tfx.googlemarket.utils.CommonUtils;
import com.tfx.googlemarket.utils.FileUtils;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.ProgressButton;

public class AppDetailDownloadHolder extends BaseHolder<AppInfoBean> implements OnClickListener, DownloadInfoObserver {

	@ViewInject(R.id.app_detail_download_btn_download)
	ProgressButton		mProgressBt;

	@ViewInject(R.id.app_detail_download_btn_favo)
	Button				mBtFavo;

	@ViewInject(R.id.app_detail_download_btn_share)
	Button				mBtShare;

	private AppInfoBean	mDatas;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_bottom, null);
		ViewUtils.inject(this, view);
		mProgressBt.setOnClickListener(this);
		mBtFavo.setOnClickListener(this);
		mBtShare.setOnClickListener(this);
		return view;
	}

	@Override
	protected void refreshHolderView(AppInfoBean data) {
		mDatas = data;
		getDownloadInfoToRefreshUi();
	}

	public void getDownloadInfoToRefreshUi() {
		//获取当前最新的downloadInfo，去刷新ui
		DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(mDatas);
		refreshProgressBtUi(downloadInfo);
	}

	private void refreshProgressBtUi(DownloadInfo info) {
		// 根据不同的状态给用户提示 处理ui  
		mProgressBt.setEnableProgress(true);
		mProgressBt.setMax(info.maxProgress);
		mProgressBt.setProgress(info.curProgress);
		mProgressBt.setProgressDrawable(R.drawable.progress_normal);
		int progress = (int) (info.curProgress * 1.0f / info.maxProgress * 100);

		switch (info.state) {
		case DownloadManager.STATE_UNDOWNLOAD:
			//未下载
			mProgressBt.setText("下载");
			break;
		case DownloadManager.STATE_DOWNLOADING:
			//下载中 
			mProgressBt.setText(progress + "%");
			break;
		case DownloadManager.STATE_PAUSEDOWNLOAD:
			//暂停下载
			mProgressBt.setText("继续下载");
			break;
		case DownloadManager.STATE_WAITINGDOWNLOAD:
			//等待下载
			mProgressBt.setText("等待中...");
			break;
		case DownloadManager.STATE_DOWNLOADFAILED:
			//下载失败
			mProgressBt.setText("重试");
			break;
		case DownloadManager.STATE_DOWNLOADED:
			//下载完成
			mProgressBt.setEnableProgress(false);
			mProgressBt.setText("安装");
			break;
		case DownloadManager.STATE_INSTALLED:
			//已安装
			mProgressBt.setText("打开");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_detail_download_btn_download:
			processClick();
			break;
		case R.id.app_detail_download_btn_favo:
			break;

		case R.id.app_detail_download_btn_share:
			break;

		default:
			break;
		}
	};

	public void processClick() {
		//获取当前状态
		DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(mDatas);
		/* 处理不同的状态的点击事件  */
		switch (info.state) {
		case DownloadManager.STATE_UNDOWNLOAD:
			//未下载  点击下载
			doDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADING:
			//下载中 点击暂停下载
			pauseDownload(info);
			break;
		case DownloadManager.STATE_PAUSEDOWNLOAD:
			//暂停下载 点击继续下载
			doDownload(info);
			break;
		case DownloadManager.STATE_WAITINGDOWNLOAD:
			//等待下载 点击取消下载
			cancelDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADFAILED:
			//下载失败 点击重新
			doDownload(info);
			break;
		case DownloadManager.STATE_DOWNLOADED:
			//下载完成 安装
			installApp(info);
			break;
		case DownloadManager.STATE_INSTALLED:
			//已安装 打开 
			openApp(info);
			break;
		default:
			break;
		}
	}

	//取消下载
	public void cancelDownload(DownloadInfo info) {
		DownloadManager.getInstance().cancel(info);
	}

	//下载app
	public void doDownload(DownloadInfo downloadInfo) {
		/*	
		//下载需要的数据bean
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.downloadUrl = datas.downloadUrl;
		downloadInfo.saveName = datas.packageName + ".apk";
		downloadInfo.savePath = FileUtils.getDir("download");
		downloadInfo.packageName = datas.packageName;
		*/
		//移步到下载管理器模块进行下载
		DownloadManager.getInstance().download(downloadInfo);
	}

	public void pauseDownload(DownloadInfo downloadInfo) {
		DownloadManager.getInstance().pause(downloadInfo);
	}

	//打开应用
	public void openApp(DownloadInfo downloadInfo) {
		CommonUtils.openApp(UIUtils.getContext(), downloadInfo.packageName);
	}

	//安装应用
	public void installApp(DownloadInfo downloadInfo) {
		File apkFile = new File(FileUtils.getDir("download"), downloadInfo.packageName + ".apk");
		CommonUtils.installApp(UIUtils.getContext(), apkFile);
	}

	//DownloadInfo改变回调
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
				refreshProgressBtUi(info);
			}
		});
	}
}
