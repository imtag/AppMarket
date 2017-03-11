package com.tfx.googlemarket.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-7
 * @desc      常用Fragment基类

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BaseFragmentCommon.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class BaseFragmentCommon extends Fragment {
	protected FragmentActivity mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		return initView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initEvent();
	}

	/**
	 * 子类事件初始化
	 */
	private void initEvent() {
		
	}

	/**
	 * 子类数据初始化
	 */
	private void initData() {
		
	}

	/**
	 * view的初始化 子类必须重写
	 * @return
	 */
	protected abstract View initView();
	
}
