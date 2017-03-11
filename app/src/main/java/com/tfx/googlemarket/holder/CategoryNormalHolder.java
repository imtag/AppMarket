package com.tfx.googlemarket.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.CategoryInfoBean;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-14
 * @desc      分类的普通holder

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class CategoryNormalHolder extends BaseHolder<CategoryInfoBean> {

	@ViewInject(R.id.item_category_icon_1)
	ImageView	mIvIcon1;
	@ViewInject(R.id.item_category_icon_2)
	ImageView	mIvIcon2;
	@ViewInject(R.id.item_category_icon_3)
	ImageView	mIvIcon3;
	@ViewInject(R.id.item_category_name_1)
	TextView	mTvName1;
	@ViewInject(R.id.item_category_name_2)
	TextView	mTvName2;
	@ViewInject(R.id.item_category_name_3)
	TextView	mTvName3;

	//初始化视图
	@Override
	protected View initHolderView() {
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
		ViewUtils.inject(this, rootView);
		return rootView;
	}

	//视图和数据绑定
	@Override
	protected void refreshHolderView(CategoryInfoBean data) {
		setData(mTvName1, data.name1, mIvIcon1, data.url1);
		setData(mTvName2, data.name2, mIvIcon2, data.url2);
		setData(mTvName3, data.name3, mIvIcon3, data.url3);
	}

	//视图数据绑定 抽取
	public void setData(TextView tv, final String name, ImageView iv, String url) {
		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)) {
			tv.setText(name);
			BitmapHelper.display(iv, URLS.IMAGEBASEURL + url);

			ViewGroup parent = (ViewGroup) tv.getParent();
			parent.setVisibility(View.VISIBLE);

			//点击事件
			parent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), name, 0).show();
				}
			});
		} else {
			//隐藏空白块
			ViewGroup parent = (ViewGroup) tv.getParent();
			parent.setVisibility(View.INVISIBLE);
		}
	}
}
