package com.tfx.googlemarket.fragment;

import java.io.IOException;
import java.util.List;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.BaseItemAdapter;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.bean.AppInfoBean;
import com.tfx.googlemarket.factory.ListViewFactory;
import com.tfx.googlemarket.protocol.ApplyProtocol;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-11
 * @desc      应用的fragment

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: ApplyFragment.java 4 2016-10-14 02:56:02Z tfx $
 */

public class ApplyFragment extends BaseFragment {

	private List<AppInfoBean>	mDatas;
	private ApplyProtocol		mApplyProtocol;
	private ApplyAdapter		mApplyAdapter;

	@Override
	public void onResume() {
		if (mApplyAdapter != null) {
			//更新界面 -- 》 会重新获取下载状态 更新ui
			mApplyAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		//获取数据
		try {
			mApplyProtocol = new ApplyProtocol();
			mDatas = mApplyProtocol.loadData(0);//加载app第一页数据
			return checkState(mDatas);
		} catch (HttpException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	//成功视图
	@Override
	protected View initSuccessView() {
		//创建listview
		ListView lv = ListViewFactory.createListView();
		//绑定适配器
		mApplyAdapter = new ApplyAdapter(lv, mDatas);
		lv.setAdapter(mApplyAdapter);
		return lv;
	}

	private class ApplyAdapter extends BaseItemAdapter {
		public ApplyAdapter(AbsListView absListView, List<AppInfoBean> datas) {
			super(absListView, datas);
		}

		/**
		 * @desc    真正子线程中加载更多数据，默认返回null，子类复写，返回具体的加载到的更多数据
		 * @desc    加载更多数据时，可能出现问题，需要抛出异常，把异常throws抛给调用处
		 * @return  返回加载到的更多数据的list集合
		 */
		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			SystemClock.sleep(1200);

			//加载更多数据
			List<AppInfoBean> loadData = mApplyProtocol.loadData(mDatas.size());
			return loadData;
		}
	}
}
