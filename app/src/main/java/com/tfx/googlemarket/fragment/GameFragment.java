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
import com.tfx.googlemarket.protocol.GameProtocol;

public class GameFragment extends BaseFragment {

	private GameProtocol		mGameProtocol;
	private List<AppInfoBean>	mDatas;
	private GameAdapter			mGameAdapter;

	@Override
	public void onResume() {
		if (mGameAdapter != null) {
			//更新界面 -- 》 会重新获取下载状态 更新ui
			mGameAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	protected LoadedResult initData() {
		try {
			//加载数据
			mGameProtocol = new GameProtocol();
			mDatas = mGameProtocol.loadData(0);
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
		ListView lv = ListViewFactory.createListView();
		mGameAdapter = new GameAdapter(lv, mDatas);
		lv.setAdapter(mGameAdapter);
		return lv;
	}

	private class GameAdapter extends BaseItemAdapter {

		public GameAdapter(AbsListView absListView, List<AppInfoBean> datas) {
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

			//加载更多数据  并返回加载到的数据集合
			List<AppInfoBean> loadData = mGameProtocol.loadData(mDatas.size());
			return loadData;
		}
	}
}