package com.tfx.googlemarket.holder.detail;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.utils.UIUtils;

public class AppDetailDescHolder extends BaseHolder<AppInfoBean> implements OnClickListener {

	@ViewInject(R.id.app_detail_des_iv_arrow)
	ImageView			mIvArrow;

	@ViewInject(R.id.app_detail_des_tv_author)
	TextView			mTvAuthor;

	@ViewInject(R.id.app_detail_des_tv_des)
	TextView			mTvDesc;

	private boolean		isOpen	= false;
	private int			mHeight;
	private AppInfoBean	datas;
	private int			mShowHeight;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
		view.setOnClickListener(this);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void refreshHolderView(AppInfoBean data) {
		datas = data;
		mTvAuthor.setText(data.author);
		mTvDesc.setText(data.des);
		//监听tv布局完成
		mTvDesc.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//获取tv高度
				mHeight = mTvDesc.getMeasuredHeight();
				//只能观察一次 
				mTvDesc.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				//默认tv高度为6行
				mShowHeight = getShowHeight(7, datas.des);
				if (mHeight <= mShowHeight) {
					mTvDesc.setHeight(mHeight);
				} else {
					mTvDesc.setHeight(mShowHeight);
				}
				ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
			}
		});
	}

	@Override
	public void onClick(View v) {
		//开启动画
		toggleAnimation();
	}

	private void toggleAnimation() {
		int start;
		int end;
		if (isOpen) {
			//打开--》折叠
			start = mHeight;
			if (mHeight <= mShowHeight) {
				end = start;
			} else {
				end = mShowHeight;
			}
			//箭头向下动画
			ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
		} else {
			//折叠--》打开
			end = mHeight;
			if (mHeight <= mShowHeight) {
				start = end;
			} else {
				start = mShowHeight;
			}
			//箭头向上动画
			ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
		}
		//设置属性动画参数
		ObjectAnimator oa = ObjectAnimator.ofInt(mTvDesc, "height", start, end);
		oa.start();
		//改变状态
		isOpen = !isOpen;

		//监听动画执行过程
		oa.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				//递归找tv的最外层的scrollview
				ViewParent parent = mTvDesc.getParent();
				while (true) {
					parent = parent.getParent();
					if (parent instanceof ScrollView) {
						//找到了外层scorllview
						ScrollView sv = (ScrollView) parent;
						//滑动到底部 
						sv.fullScroll(View.FOCUS_DOWN);
						break;
					}
					if (parent == null) {
						break;
					}
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * @desc 获取7行的textview的高度值
	 */
	protected int getShowHeight(int lineNum, String content) {
		TextView tempTv = new TextView(UIUtils.getContext());
		tempTv.setLines(lineNum);// 需要设置行高才可以拿到具体的高度
		tempTv.setText(content);
		tempTv.measure(0, 0);
		int tempHeight = tempTv.getMeasuredHeight();
		return tempHeight;
	}
}
