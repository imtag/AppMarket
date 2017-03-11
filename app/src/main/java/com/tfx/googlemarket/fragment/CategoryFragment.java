package com.tfx.googlemarket.fragment;

import java.io.IOException;
import java.util.List;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.base.SuperBaseAdapter;
import com.tfx.googlemarket.bean.CategoryInfoBean;
import com.tfx.googlemarket.factory.ListViewFactory;
import com.tfx.googlemarket.holder.CategoryNormalHolder;
import com.tfx.googlemarket.holder.CategoryTitleHolder;
import com.tfx.googlemarket.protocol.CategoryProtocol;

public class CategoryFragment extends BaseFragment {

	private List<CategoryInfoBean>	mDatas;

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		CategoryProtocol categoryProtocol = new CategoryProtocol();
		try {
			mDatas = categoryProtocol.loadData(0);
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
		lv.setAdapter(new CategoryAdapter(lv, mDatas));
		return lv;
	}

	class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {

		public CategoryAdapter(AbsListView absListView, List<CategoryInfoBean> datas) {
			super(absListView, datas);
		}

		@Override
		protected BaseHolder<CategoryInfoBean> getSpecialHolder(int position) {
			//判断当前条目是标题还是普通
			if (mDatas.get(position).isTitle) {
				//标题
				return new CategoryTitleHolder();
			} else {
				//普通
				return new CategoryNormalHolder();
			}
		}

		@Override
		protected boolean hasLoadMore() {
			return false;
		}
		
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1; //父类默认2（loadMore和普通） + 1 = 3
		}
		
		@Override
		protected int getNormalItemViewType(int position) {
			CategoryInfoBean bean = mDatas.get(position);
			if(bean.isTitle){
				//当前条目是标题
				return 1; //不能是0  0是加载更多
			}else{
				//当前条目是普通
				return 2;
			}
		}
	}
}