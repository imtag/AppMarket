package com.tfx.googlemarket.holder.detail;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.bean.AppInfoBean.Safe;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.UIUtils;

public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> {

	@ViewInject(R.id.app_detail_safe_des_container)
	LinearLayout	mLlDesc;

	@ViewInject(R.id.app_detail_safe_pic_container)
	LinearLayout	mLlPic;

	@ViewInject(R.id.app_detail_safe_iv_arrow)
	ImageView		mIvArrow;

	private boolean	isOpen	= false;
	private View	mView;

	@Override
	protected View initHolderView() {
		mView = View.inflate(UIUtils.getContext(), R.layout.item_detail_safe, null);
		ViewUtils.inject(this, mView);
		return mView;
	}

	@Override
	protected void refreshHolderView(AppInfoBean data) {
		List<Safe> safes = data.safe;
		for (int i = 0; i < safes.size(); i++) {
			//数据
			Safe safe = safes.get(i);

			//图片信息
			ImageView iv2 = new ImageView(UIUtils.getContext());
			BitmapHelper.display(iv2, URLS.IMAGEBASEURL + safe.safeUrl);
			mLlPic.addView(iv2);

			//文字描述
			LinearLayout line = new LinearLayout(UIUtils.getContext());
			line.setGravity(Gravity.CENTER_VERTICAL);

			ImageView iv = new ImageView(UIUtils.getContext());
			int padding = UIUtils.dp2px(UIUtils.getContext(), 8);
			int padding2 = UIUtils.dp2px(UIUtils.getContext(), 2);
			iv.setPadding(padding, 0, 0, padding2);
			BitmapHelper.display(iv, URLS.IMAGEBASEURL + safe.safeDesUrl);

			TextView tv = new TextView(UIUtils.getContext());
			int color = safe.safeDesColor;
			if (color == 0) {
				tv.setTextColor(Color.GRAY);
			} else {
				tv.setTextColor(Color.RED);
			}
			tv.setText(safe.safeDes);

			line.addView(iv);
			line.addView(tv);
			mLlDesc.addView(line);
		}

		//初始化ll为关闭
		LayoutParams lp = mLlDesc.getLayoutParams();
		lp.height = 0;
		mLlDesc.setLayoutParams(lp);
		//初始化箭头方向向上
		ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();

		//箭头点击事件
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//开启动画
				toggleAnimation();
				//改变状态
				isOpen = !isOpen;
			}
		});
	}

	protected void toggleAnimation() {
		//测量ll高度，0==不指定宽高，有多大测多大
		mLlDesc.measure(0, 0);
		int start;
		int end;
		if (isOpen) {
			//打开--》关闭
			start = mLlDesc.getMeasuredHeight();
			end = 0;
			//箭头旋转
			ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
		} else {
			//关闭--》打开
			start = 0;
			end = mLlDesc.getMeasuredHeight();
			//箭头旋转
			ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
		}

		//创建属性动画
		ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
		valueAnimator.start();

		//监听  实时更新ll的高度
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				LayoutParams lp = mLlDesc.getLayoutParams();
				lp.height = (Integer) animation.getAnimatedValue();//差值
				mLlDesc.setLayoutParams(lp);
			}
		});
	}
}
