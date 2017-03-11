package com.tfx.googlemarket.holder;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.config.Constants.URLS;
import com.tfx.googlemarket.utils.BitmapHelper;
import com.tfx.googlemarket.utils.UIUtils;
import com.tfx.googlemarket.view.InnerViewPager;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-12
 * @desc      首页轮播图holder

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: HomePictureHolder.java 4 2016-10-14 02:56:02Z tfx $
 */

public class HomePictureHolder extends BaseHolder<List<String>> {

	private LinearLayout	mIndicatorContainer;
	private InnerViewPager		mViewPager;
	private List<String>	mDatas;

	@Override
	protected View initHolderView() {
		//轮播图布局
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_home_pictures, null);
		mIndicatorContainer = (LinearLayout) rootView.findViewById(R.id.item_home_picture_container_indicator);
		mViewPager = (InnerViewPager) rootView.findViewById(R.id.item_home_picture_pager);
		return rootView;
	}

	@Override
	protected void refreshHolderView(List<String> data) {
		//保存数据
		mDatas = data;
		//绑定适配器
		mViewPager.setAdapter(new PictureAdapter());

		//加上点
		for (int i = 0; i < mDatas.size(); i++) {
			//初始化点的imageview
			ImageView iv = new ImageView(UIUtils.getContext());
			LayoutParams lp = new LinearLayout.LayoutParams(UIUtils.dp2px(UIUtils.getContext(), 6), UIUtils.dp2px(UIUtils.getContext(), 6));
			lp.leftMargin = UIUtils.dp2px(UIUtils.getContext(), 3);
			lp.rightMargin = UIUtils.dp2px(UIUtils.getContext(), 3);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.pointer_gray);
			if (i == 0) {
				iv.setImageResource(R.drawable.pointer_red);
			} 
			//将点添加到容器
			mIndicatorContainer.addView(iv);
		}
		
		//viewpager页面改变监听  选中点为当前页面
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				position = position % mDatas.size();
				
				//1.初始化所有点为未选中
				for (int i = 0; i < mDatas.size(); i++) {
					ImageView view = (ImageView) mIndicatorContainer.getChildAt(i);
					view.setImageResource(R.drawable.pointer_gray);
				}
				//2.设置当前页面点为选中
				ImageView iv = (ImageView) mIndicatorContainer.getChildAt(position);
				iv.setImageResource(R.drawable.pointer_red);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		}); 
		
		//设置当前选中索引
		//首次运行为了让positon=0，需求出偏差  求出当Integer.MAX_VALUE/2时% mDatas.size()得值
		int diff = Integer.MAX_VALUE / 2 % mDatas.size(); 
		//让轮播图能往左右无限滑 会接着调用instantiateItem 此时position就是Integer.MAX_VALUE/2
		//首次运行需要让position % mDatas.size()==0 ， 需要减去偏差
		mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - diff);  
		
		//自动轮播
		final AutoScrollTask autoScrollTask = new AutoScrollTask();
		autoScrollTask.start();
		
		//按下去停止轮播  抬起继续轮播
		mViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//停止轮播
					autoScrollTask.stop();
					break;
					
				case MotionEvent.ACTION_UP:
					//继续轮播
					autoScrollTask.start();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
 
	//自动轮播的任务 耗时 需要开任务
	class AutoScrollTask implements Runnable{
		public void start(){ 
			UIUtils.getMainThreadHandler().postDelayed(this, 2000);
		}
		
		public void stop(){
			UIUtils.getMainThreadHandler().removeCallbacks(this);
		}
		
		@Override
		public void run() {
			//自动轮播
			int currentItem = mViewPager.getCurrentItem();
			currentItem++;
			mViewPager.setCurrentItem(currentItem);
			
			//递归调用 无限循环
			start();
		}
		
	}
	
	//viewpager适配器
	private class PictureAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mDatas != null) {
				return Integer.MAX_VALUE; //无限往右滑动
			}
			return 0;
		} 

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object; 
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//因为getcount值很大 所以要让索引为0-mDatas.size()之间
			position = position % mDatas.size();
			
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ScaleType.FIT_XY);
			//加载图片
			BitmapHelper.display(iv, URLS.IMAGEBASEURL + mDatas.get(position));
			container.addView(iv);

			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
