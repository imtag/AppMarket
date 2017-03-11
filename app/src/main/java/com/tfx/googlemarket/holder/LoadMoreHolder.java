package com.tfx.googlemarket.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-9
 * @desc      LoadMore视图持有者

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: LoadMoreHolder.java 4 2016-10-14 02:56:02Z tfx $
 */

public class LoadMoreHolder extends BaseHolder<Integer> {

	private RelativeLayout	mRlLoading;
	private RelativeLayout	mRlRetry;
	private TextView		mTvRetry;

	public static final int	LOADMORE_LOADING	= 0;	//加载中状态
	public static final int	LOADMORE_RETRY		= 1;	//加载失败 重新加载
	public static final int	LOADMORE_NONE		= 2;	//都不显示

	//布局初始化
	@Override
	protected View initHolderView() {
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);
		mRlLoading = (RelativeLayout) rootView.findViewById(R.id.item_loadmore_container_loading);
		mRlRetry = (RelativeLayout) rootView.findViewById(R.id.item_loadmore_container_retry);
		mTvRetry = (TextView) rootView.findViewById(R.id.item_loadmore_tv_retry);
		return rootView;
	}

	//根据传入的状态值  控制加载中 重新加载 的显示隐藏
	@Override
	public void refreshHolderView(Integer currentState) {
		//所有隐藏
		mRlLoading.setVisibility(8);
		mRlRetry.setVisibility(8);

		//根据当前状态控制视图显示
		switch (currentState) {
		case LOADMORE_LOADING:
			//显示加载中
			mRlLoading.setVisibility(0);
			break;
		case LOADMORE_RETRY:
			//显示加载失败 重新加载
			mRlRetry.setVisibility(0);
			break;
		case LOADMORE_NONE:
			//数据为空  都不显示
			break;

		default:
			break;
		}
	}
}
