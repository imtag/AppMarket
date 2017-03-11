package com.tfx.googlemarket.base;

import java.util.ArrayList;
import java.util.List;

import com.tfx.googlemarket.activity.HomeActivity;
import com.tfx.googlemarket.utils.UIUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-18
 * @desc      activity基类

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public abstract class BaseActivity extends FragmentActivity {
	private List<Activity>	activitys	= new ArrayList<Activity>();
	private long			mPreTime;
	private Activity		mCurActivity;

	/*
	 * 得到最上层的Activity
	 */
	public Activity getCurActivity() {
		return mCurActivity;
	}

	@Override
	protected void onResume() {
		//获取到最上层的Activity
		mCurActivity = this;
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initActionBar();
		initData();
		initEvent();
		activitys.add(this); //创建一个activity，将其添加到集合
	}

	@Override
	protected void onDestroy() {
		activitys.remove(this);
		this.exit();
		super.onDestroy();
	}

	//完全退出
	public void exit() {
		for (Activity activity : activitys) {
			activity.finish();
		}
	}

	/* 
	 * 首页按两次back键才退出
	 */
	@Override
	public void onBackPressed() {
		if (this instanceof HomeActivity) {
			if (System.currentTimeMillis() - mPreTime > 2000) {
				Toast.makeText(UIUtils.getContext(), "再按一次，退出谷歌市场", 0).show();
				mPreTime = System.currentTimeMillis();
				return;
			} else {
				this.finish();
			}
		}
		//不是首页
		super.onBackPressed(); // == finish
	}

	protected void initEvent() {

	}

	protected void initData() {

	}

	protected void initActionBar() {

	}

	protected abstract void initView();
}
