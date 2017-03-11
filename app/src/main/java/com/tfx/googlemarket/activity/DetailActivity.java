package com.tfx.googlemarket.activity;

import java.io.IOException;

import android.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseActivity;
import com.tfx.googlemarket.base.LoadingPager;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.holder.detail.AppDetailDescHolder;
import com.tfx.googlemarket.holder.detail.AppDetailDownloadHolder;
import com.tfx.googlemarket.holder.detail.AppDetailInfoHolder;
import com.tfx.googlemarket.holder.detail.AppDetailPicHolder;
import com.tfx.googlemarket.holder.detail.AppDetailSafeHolder;
import com.tfx.googlemarket.manager.DownloadManager;
import com.tfx.googlemarket.protocol.DetailProtocol;
import com.tfx.googlemarket.utils.UIUtils;

public class DetailActivity extends BaseActivity {
	private AppInfoBean				mAppInfoBean;
	private LoadingPager			mLoadingPager;

	@ViewInject(R.id.detail_fl_bottom)
	FrameLayout						mContainerBottom;

	@ViewInject(R.id.detail_fl_des)
	FrameLayout						mContainerDes;

	@ViewInject(R.id.detail_fl_info)
	FrameLayout						mContainerInfo;

	@ViewInject(R.id.detail_fl_pic)
	FrameLayout						mContainerPic;

	@ViewInject(R.id.detail_fl_safe)
	FrameLayout						mContainerSafe;
	private AppDetailDownloadHolder	mAppDetailDownloadHolder;

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void initData() {
		//触发加载数据
		mLoadingPager.triggerLoadData();
	}

	@Override
	protected void initView() {
		mLoadingPager = new LoadingPager(UIUtils.getContext()) {
			@Override
			protected View initSuccessView() {
				return DetailActivity.this.initSuccessView();
			}

			@Override
			protected LoadedResult initData() {
				//加载数据
				return DetailActivity.this.loadData();
			}
		};
		//设置内容视图
		setContentView(mLoadingPager);
	}

	protected LoadedResult loadData() {
		String mPackageName = getIntent().getStringExtra("packageName");
		DetailProtocol detailProtocol = new DetailProtocol(mPackageName);
		try {
			mAppInfoBean = detailProtocol.loadData(0);
			if (mAppInfoBean != null) {
				return LoadedResult.SUCCESS;
			} else {
				return LoadedResult.EMPTY;
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	protected View initSuccessView() {
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_detail, null);
		//找出孩子
		ViewUtils.inject(this, rootView);

		//接收holder提供的视图

		//信息部分
		AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder();
		mContainerInfo.addView(appDetailInfoHolder.mHolderView);
		appDetailInfoHolder.setDataToRefreshHolderView(mAppInfoBean);

		//安全部分
		AppDetailSafeHolder appDetaiSafeHolder = new AppDetailSafeHolder();
		mContainerSafe.addView(appDetaiSafeHolder.mHolderView);
		appDetaiSafeHolder.setDataToRefreshHolderView(mAppInfoBean);

		//截图部分
		AppDetailPicHolder appDetaiPicHolder = new AppDetailPicHolder();
		mContainerPic.addView(appDetaiPicHolder.mHolderView);
		appDetaiPicHolder.setDataToRefreshHolderView(mAppInfoBean);

		//描述部分
		AppDetailDescHolder appDetailDescHolder = new AppDetailDescHolder();
		mContainerDes.addView(appDetailDescHolder.mHolderView);
		appDetailDescHolder.setDataToRefreshHolderView(mAppInfoBean);

		//下载部分
		mAppDetailDownloadHolder = new AppDetailDownloadHolder();
		mContainerBottom.addView(mAppDetailDownloadHolder.mHolderView);
		mAppDetailDownloadHolder.setDataToRefreshHolderView(mAppInfoBean);
		//添加appDetailDownloadHolder为观察者
		DownloadManager.getInstance().addObserver(mAppDetailDownloadHolder);

		return rootView;
	}

	@Override
	protected void onPause() {
		//移除观察者
		if (mAppDetailDownloadHolder != null) {
			DownloadManager.getInstance().deleteObserver(mAppDetailDownloadHolder);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mAppDetailDownloadHolder != null) {
			//添加观察者
			DownloadManager.getInstance().addObserver(mAppDetailDownloadHolder);
			//获取最新状态 手动 刷新ui
			mAppDetailDownloadHolder.getDownloadInfoToRefreshUi();
		}
		super.onResume();
	}
}
