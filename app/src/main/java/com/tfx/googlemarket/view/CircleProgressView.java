package com.tfx.googlemarket.view;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleProgressView extends LinearLayout {

	private ImageView	mIcon;
	private TextView	mText;
	private long		mMax;
	private long		mProgress;
	private boolean		EnableProgress;

	public void setMax(long max) {
		mMax = max;
	}

	public void setIcon(int res) {
		mIcon.setImageResource(res);
	}

	public void setText(String note) {
		mText.setText(note);
	}

	public void setEnableProgress(boolean enableProgress) {
		EnableProgress = enableProgress;
	}

	public void setProgress(long progress) {
		mProgress = progress;
		invalidate();
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.circleprogressview, this);
		mIcon = (ImageView) view.findViewById(R.id.circleProgressView_iv);
		mText = (TextView) view.findViewById(R.id.circleProgressView_tv);
	}

	public CircleProgressView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);//画背景 
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);// 绘制具体的内容(图片和文字)
		if (EnableProgress) {
			RectF oval = new RectF(mIcon.getLeft(), mIcon.getTop(), mIcon.getRight(), mIcon.getBottom());
			float startAngle = -90;
			float sweepAngle = mProgress * 360.f / mMax;
			boolean useCenter = false; //是否保留两条边
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.BLUE);
			paint.setStrokeWidth(UIUtils.dp2px(UIUtils.getContext(), 3));
			paint.setAntiAlias(true);//消除锯齿
			canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
		}
	}
}
