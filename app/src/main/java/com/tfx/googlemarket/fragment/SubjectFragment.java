package com.tfx.googlemarket.fragment;

import java.io.IOException;
import java.util.List;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.base.SuperBaseAdapter;
import com.tfx.googlemarket.bean.SubjectInfoBean;
import com.tfx.googlemarket.factory.ListViewFactory;
import com.tfx.googlemarket.holder.SubjectHolder;
import com.tfx.googlemarket.protocol.SubjectProtocol;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-12
 * @desc      专题fragment

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: SubjectFragment.java 4 2016-10-14 02:56:02Z tfx $
 */

public class SubjectFragment extends BaseFragment {

	private List<SubjectInfoBean>	mDatas;

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		SystemClock.sleep(1200);

		SubjectProtocol subjectProtocol = new SubjectProtocol();
		try {
			mDatas = subjectProtocol.loadData(0);
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
		lv.setAdapter(new SubjectAdapter(lv, mDatas));
		return lv;
	}

	class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {

		public SubjectAdapter(AbsListView absListView, List<SubjectInfoBean> datas) {
			super(absListView, datas);
		}

		@Override
		protected BaseHolder<SubjectInfoBean> getSpecialHolder(int position) {
			return new SubjectHolder();
		}

		//没有加载更多
		@Override
		protected boolean hasLoadMore() {
			return false;
		}
	}
}