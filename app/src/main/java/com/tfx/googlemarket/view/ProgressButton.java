package com.tfx.googlemarket.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-18
 * @desc      自定义带进度的按钮

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class ProgressButton extends Button {

	private long	mMax	= 100;
	private long	mProgress;
	private boolean	mEnableProgress;

	private int		mProgressDrawable;

	public void setProgressDrawable(int progressDrawable) {
		mProgressDrawable = progressDrawable;
	}

	public void setMax(long max) {
		mMax = max;
	}

	public void setProgress(long progress) {
		this.mProgress = progress;
		invalidate(); //刷新界面
	}

	public void setEnableProgress(boolean enableProgress) {
		this.mEnableProgress = enableProgress;
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProgressButton(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mEnableProgress) {
			Drawable drawable = new ColorDrawable(mProgressDrawable);
			int left = 0;
			int top = 0;
			int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + 0.5f);
			int bottom = getBottom();

			drawable.setBounds(left, top, right, bottom);// 必须的.告知绘制的范围
			drawable.draw(canvas);
		}
		super.onDraw(canvas);// 绘制文本,还会绘制背景
	}

}
