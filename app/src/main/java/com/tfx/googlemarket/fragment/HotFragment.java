package com.tfx.googlemarket.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.protocol.HotProtocol;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.FlowLayout;

public class HotFragment extends BaseFragment {

	private List<String>	mDatas;

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		try {
			HotProtocol hotProtocol = new HotProtocol();
			mDatas = hotProtocol.loadData(0);
			return checkState(mDatas);
		} catch (HttpException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	@Override
	protected View initSuccessView() {
		int padding = UIUtils.dp2px(UIUtils.getContext(), 6);
		int padding2 = UIUtils.dp2px(UIUtils.getContext(), 10);

		ScrollView sv = new ScrollView(UIUtils.getContext());
		FlowLayout fl = new FlowLayout(UIUtils.getContext());
		fl.setPadding(padding, padding, padding, padding);
		for (final String info : mDatas) {
			TextView tv = new TextView(UIUtils.getContext());
			tv.setPadding(padding2, padding, padding2, padding);
			tv.setText(info);
			tv.setTextColor(Color.WHITE);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(15);

			//默认效果
			GradientDrawable normalBg = new GradientDrawable();//可设置圆角的图片
			Random random = new Random();
			int alpha = 255;
			int red = random.nextInt(170) + 30; //30 - 170
			int green = random.nextInt(170) + 30;
			int blue = random.nextInt(170) + 30;
			int argb = Color.argb(alpha, red, green, blue);
			normalBg.setColor(argb);
			normalBg.setCornerRadius(8); //圆角

			//按下效果
			GradientDrawable pressBg = new GradientDrawable();
			pressBg.setColor(Color.DKGRAY);
			pressBg.setCornerRadius(8);

			StateListDrawable sld = new StateListDrawable();
			sld.addState(new int[] { android.R.attr.state_pressed }, pressBg); //按下状态
			sld.addState(new int[] {}, normalBg); //默认状态

			tv.setClickable(true);
			tv.setBackgroundDrawable(sld);

			//单击事件
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), info, 0).show();
				}
			});

			fl.addView(tv);
		}
		sv.addView(fl);
		return sv;
	}
}