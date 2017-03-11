package com.tfx.googlemarket.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.protocol.RecommendProtocol;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.flyinout.ShakeListener;
import com.tfx.googlemarket.view.flyinout.ShakeListener.OnShakeListener;
import com.tfx.googlemarket.view.flyinout.StellarMap;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-16
 * @desc      推荐页面fragment

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class RecommendFragment extends BaseFragment {

	private List<String>		mDatas;
	private RecommendAdapter	mRecommendAdapter;
	private ShakeListener		mShakeListener;

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		RecommendProtocol recommendProtocol = new RecommendProtocol();
		try {
			mDatas = recommendProtocol.loadData(0);
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
	public void onPause() {
		if (mShakeListener != null) {
			mShakeListener.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (mShakeListener != null) {
			mShakeListener.resume();
		}
		super.onResume();
	}

	@Override
	protected View initSuccessView() {
		final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
		mRecommendAdapter = new RecommendAdapter();
		stellarMap.setAdapter(mRecommendAdapter);

		//设置拆分规则
		stellarMap.setRegularity(15, 20);

		//设置首页选中
		stellarMap.setGroup(0, true);

		//加入摇一摇
		mShakeListener = new ShakeListener(UIUtils.getContext());
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				int currentGroup = stellarMap.getCurrentGroup();
				if (currentGroup == mRecommendAdapter.getGroupCount() - 1) {
					//最后一组
					currentGroup = 0;
				} else {
					//到下一组
					currentGroup++;
				}
				//切换
				stellarMap.setGroup(currentGroup, true);
			}
		});

		return stellarMap;
	}

	private class RecommendAdapter implements StellarMap.Adapter {
		private static final int	PAGESIZE	= 20;

		@Override
		public int getGroupCount() {
			//返回多少组
			if (mDatas.size() % PAGESIZE != 0) {
				//有余数
				return mDatas.size() / PAGESIZE + 1;
			}
			//没余数
			return mDatas.size() / PAGESIZE;
		}

		@Override
		public int getCount(int group) {
			//每组多少个
			if (group == getGroupCount() - 1) {
				//最后一页
				if (mDatas.size() % PAGESIZE != 0) {
					//显示余数
					return mDatas.size() % PAGESIZE;
				}
			}
			return PAGESIZE;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			//具体view
			TextView tv = new TextView(UIUtils.getContext());
			final int location = group * PAGESIZE + position;
			tv.setText(mDatas.get(location));

			int padding = UIUtils.dp2px(UIUtils.getContext(), 5);
			tv.setPadding(padding, padding, padding, padding);
			//随机字体大
			Random random = new Random();
			tv.setTextSize(random.nextInt(4) + 14);
			//随机颜色
			int red = random.nextInt(190) + 30;
			int green = random.nextInt(190) + 30;
			int blue = random.nextInt(190) + 30;
			int color = Color.rgb(red, green, blue);
			tv.setTextColor(color);
			//事件监听
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), mDatas.get(location), 0).show();
				}
			});
			return tv;
		}

		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			return 0;
		}
	}

}