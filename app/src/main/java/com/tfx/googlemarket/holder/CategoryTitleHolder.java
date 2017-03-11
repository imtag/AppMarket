package com.tfx.googlemarket.holder;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.CategoryInfoBean;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-14
 * @desc      分类标题的holder 视图+data

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

	private TextView	mTv;

	//初始化视图
	@Override
	protected View initHolderView() {
		mTv = new TextView(UIUtils.getContext());
		int padding = UIUtils.dp2px(UIUtils.getContext(), 8);
		mTv.setTextSize(16);
		mTv.setTextColor(Color.BLACK);
		//		mTv.setGravity(Gravity.CENTER);
		mTv.setBackgroundColor(Color.WHITE);
		mTv.setPadding(padding, padding, padding, padding);
		return mTv;
	}

	//视图和数据绑定
	@Override
	protected void refreshHolderView(CategoryInfoBean data) {
		mTv.setText(data.title);
	}
}
