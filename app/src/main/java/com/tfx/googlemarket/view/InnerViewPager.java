package com.tfx.googlemarket.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InnerViewPager extends ViewPager {

	private float	mDownX;
	private float	mDownY;

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getX();
			float moveY = ev.getY();
			
			float dx = Math.abs(mDownX - moveX);
			float dy = Math.abs(mDownY - moveY);

			if (dx > dy) {
				//横向滑动 申请父控件不拦截事件
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {
				//纵向滑动 申请父控件拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
}
