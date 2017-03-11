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
import com.tfx.googlemarket.bean.HomeBean;
import com.tfx.googlemarket.factory.ListViewFactory;
import com.tfx.googlemarket.holder.HomePictureHolder;
import com.tfx.googlemarket.protocol.HomeProtocol;

public class HomeFragment extends BaseFragment {

	private List<String>		mPictures;
	private List<AppInfoBean>	mAppInfoBeans;
	private HomeProtocol		mHomeProtocol;
	private HomeAdapter			mHomeAdapter;

	@Override
	public void onResume() {
		if (mHomeAdapter != null) {
			//更新界面 -- 》 会重新获取下载状态 更新ui
			mHomeAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	protected LoadedResult initData() {
		//加载数据 子线程
		try {
			mHomeProtocol = new HomeProtocol();
			HomeBean homeBean = mHomeProtocol.loadData(0);//加载首页第一页数据

			LoadedResult state = checkState(homeBean); //homeBean非空校验
			if (state != LoadedResult.SUCCESS) {
				//homeBean有问题
				return state;
			}

			state = checkState(homeBean.list); //homeBean.list非空校验
			if (state != LoadedResult.SUCCESS) {
				//homeBean.list有问题
				return state;
			}

			//走到这里 数据没问题   赋值操作  
			mAppInfoBeans = homeBean.list;
			mPictures = homeBean.picture;

			return state;
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
		//创建listview
		ListView lv = ListViewFactory.createListView();

		//添加轮播图
		HomePictureHolder homeHeadHolder = new HomePictureHolder();
		lv.addHeaderView(homeHeadHolder.mHolderView);
		//把数据传递给homeHeadHolder做显示
		homeHeadHolder.setDataToRefreshHolderView(mPictures);
		//绑定适配器
		mHomeAdapter = new HomeAdapter(lv, mAppInfoBeans);
		lv.setAdapter(mHomeAdapter);
		return lv;
	}

	private class HomeAdapter extends BaseItemAdapter {
		public HomeAdapter(AbsListView absListView, List<AppInfoBean> datas) {
			super(absListView, datas);
			// TODO Auto-generated constructor stub
		}

		/**
		 * @desc    真正子线程中加载更多数据，默认返回null，复写，返回具体的加载到的更多数据
		 * @desc    加载更多数据时，可能出现问题，需要抛出异常，把异常throws抛给调用处
		 * @return  返回加载到的更多数据的list集合
		 */
		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			SystemClock.sleep(1200);

			//1.网络请求数据   http://localhost:8080/GooglePlayServer/home?index=当前数据条目总数 要加载第二页index=20 第三页index=40
			HomeBean homeBean = mHomeProtocol.loadData(mAppInfoBeans.size());
			return homeBean != null ? homeBean.list : null;

			/*			
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configTimeout(5000);
			String url = URLS.BASEURL + "home";
			RequestParams params = new RequestParams(); //添加请求参数 ？号后面的
			params.addQueryStringParameter("index", mAppInfoBeans.size() + "");
			ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
			String jsonString = responseStream.readString(); //流转string

			//2.解析json
			Gson gson = new Gson();
			HomeBean homeBean = gson.fromJson(jsonString, HomeBean.class);

			return homeBean != null ? homeBean.list : null;
			*/
		}
	}
}