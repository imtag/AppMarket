package com.tfx.googlemarket.activity;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStripExpand;
import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseActivity;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.LoadingPager;
import com.tfx.googlemarket.factory.FragmentFactory;
import com.tfx.googlemarket.holder.MenuHolder;
import com.tfx.googlemarket.utils.LogUtils;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-7
 * @desc      首页界面

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: HomeActivity.java 4 2016-10-14 02:56:02Z tfx $
 */

public class HomeActivity extends BaseActivity {

	private PagerSlidingTabStripExpand	mTabs;
	private ViewPager					mViewPager;
	private Context						mContext;
	private String[]					mMainTitles;

	@Override
	protected void initEvent() {
		//页面改变监听  页面选中就加载当前页面数据  
		mTabs.setOnPageChangeListener(mOnPageChangeListener);

		//每次进入程序时不会执行事件  要手动触发事件  完成视图显示
		//视图展示是需要时间的  如果直接手动触发 会空指针  需要观察视图树  视图初始化完成才能触发事件
		mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//手动触发onPageSelected方法
				mOnPageChangeListener.onPageSelected(0);
				//移除观察 只观察一次 
				mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	OnPageChangeListener			mOnPageChangeListener	= new MyOnPageChangeListener();
	private DrawerLayout			mDrawerLayout;
	private ActionBarDrawerToggle	mToggle;
	private FrameLayout				mMFlMenu;

	//页面改变监听  页面选中就加载当前页面数据  
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			//触发loadingPager加载数据
			//此时获取的baseFragment是已经存在map集合中的fragment
			BaseFragment baseFragment = FragmentFactory.createFragment(position);
			if (baseFragment != null) {
				//获取LoadingPager 触发加载数据
				LoadingPager loadingPager = baseFragment.getLoadingPager();
				if (loadingPager != null) {
					loadingPager.triggerLoadData();
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	@Override
	protected void initData() {
		//从资源文件取得页签数据
		mMainTitles = UIUtils.getStringArr(R.array.main_titles);

		//绑定适配器
		mainFragmentStatePagerAdapter adapter = new mainFragmentStatePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);

		//给页签绑定viewpager
		mTabs.setViewPager(mViewPager);

		MenuHolder menuHolder = new MenuHolder();
		mMFlMenu.addView(menuHolder.mHolderView);
		menuHolder.setDataToRefreshHolderView(null);//触发加载数据
	}

	/* 
	 * viewpager适配器  FragmentPagerAdapter是pagerAdapter的子类
	 * 
	 * FragmentStatePagerAdapter和FragmentPagerAdapter的区别
	 *     FragmentPagerAdapter:Fragment只初始化一次，会缓存fragment在内存中，适合相对静态的页面，数量也比较少的。
	 *     FragmentStatePagerAdapter：只保留当前页面，离开页面，就释放其资源，页面需要显示时，生成新的页面，页面数量多时，必须占用大量内存
	 */
	private class mainFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

		public mainFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			LogUtils.d(this.getClass().getSimpleName(), "fragment初始化了" + mMainTitles[position]);
			return FragmentFactory.createFragment(position);
		}

		@Override
		public int getCount() {
			if (mMainTitles != null) {
				return mMainTitles.length;
			}
			return 0;
		}

		/*
		 * 页签的显示 必须覆盖
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}
	}

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		initActionBarToggle();
		super.initActionBar();
	}

	private void initActionBarToggle() {
		mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_am, 0, 0);
		// 同步状态的方法
		mToggle.syncState();
		// 设置drawerLayout拖动的监听
		mDrawerLayout.setDrawerListener(mToggle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// mToggle控制打开关闭drawlayout
			mToggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		mContext = this;
		mTabs = (PagerSlidingTabStripExpand) findViewById(R.id.psts_main);
		mViewPager = (ViewPager) findViewById(R.id.vp_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawlayout);
		mMFlMenu = (FrameLayout) findViewById(R.id.fl_menu);
	}
}
