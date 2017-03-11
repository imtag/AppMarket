package com.tfx.googlemarket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tfx.googlemarket.R;

public class RatioLayout extends FrameLayout {
	private float			mPicRatio		= 0.0f;
	private int				relative		= RELATIVE_WIDTH;	//默认已知宽度 动态算出高度
	public static final int	RELATIVE_WIDTH	= 0;				//已知宽度
	public static final int	RELATIVE_HEIGHT	= 1;				//已知高度

	public void setPicRatio(float picRatio) {
		mPicRatio = picRatio;
	}

	public void setRelative(int relative) {
		this.relative = relative;
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//取出自定义属性
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

		mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_ratio, 0.0f);

		relative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);

		typedArray.recycle();
	}

	public RatioLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取宽度的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		//已知宽度   动态求出高度
		if (widthMode == MeasureSpec.EXACTLY && relative == RELATIVE_WIDTH) {
			//1.获取父控件宽
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

			//2.计算孩子宽高
			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight(); //需减去padding
			int childHeight = (int) (childWidth / mPicRatio + .5f); //根据图片宽高比算出图片高

			//3.根据孩子高度，算出父控件高度
			int parentHeight = childHeight + getPaddingBottom() + getPaddingTop(); //图片高度加上padding

			//4.根据布局高度和高度模式，重新定义高度测量模式
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);

		} else if (heightMode == MeasureSpec.EXACTLY && relative == RELATIVE_HEIGHT) {
			//1.获取父控件高
			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

			//2.计算孩子宽高
			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop(); //需减去padding
			//根据图片宽高比算出图片高
			int childWidth = (int) (childHeight * mPicRatio + .5f);

			//3.根据孩子宽度，算出父控件宽度
			int parentWidth = childWidth + getPaddingRight() + getPaddingLeft(); //图片高度加上padding

			//4.根据布局宽度和宽度模式，重新定义宽度测量模式
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
		}

		//根据最新的宽高去测量
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
