package com.tfx.googlemarket.base;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tfx.googlemarket.factory.ThreadPoolExecutorProxyFactory;
import com.tfx.googlemarket.holder.LoadMoreHolder;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-8
 * @desc      BaseAdapter基类

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: SuperBaseAdapter.java 4 2016-10-14 02:56:02Z tfx $
 */

// implements OnItemClickListener为了重写条目单击事件的方法
public abstract class SuperBaseAdapter<T> extends BaseAdapter implements OnItemClickListener {

	protected List<T>		mDatas;
	public static final int	VIEWTYPE_LOADMORE	= 0;
	public static final int	VIEWTYPE_NORMAL		= 1;
	private LoadMoreHolder	mLoadMoreHolder;
	private LoadMoreTask	mLoadMoreTask;
	private AbsListView		mAbsListView;

	/*
	 * 构造器  
	 * 		数据  --> 用来数据和视图绑定
	 * 		当前listview或gridview的父类(absListView) --> 用来设置监听 调用事件
	 */
	public SuperBaseAdapter(AbsListView absListView, List<T> datas) {
		this.mAbsListView = absListView;
		this.mDatas = datas;

		//给传过来的absListVIew设置监听
		mAbsListView.setOnItemClickListener(this);
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size() + 1; //需要+1 用来显示加载更多
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 得到viewType的总数
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount() + 1; //默认一种 +1 两种
	}

	/**
	 * 得到指定position对应的item的viewType类型
	 */
	@Override
	public int getItemViewType(int position) {
		if (position == getCount() - 1) {
			//当前是最后一条数据
			return VIEWTYPE_LOADMORE; //0  显示加载更多
		} else {
			return getNormalItemViewType(position);
		}
	}

	/**
	 * @desc 默认返回的是1， 子类可以复写该方法，返回更多的普通条目的viewtype。
	 * @call 子类里面的ViewType超过两种的时候
	 */
	protected int getNormalItemViewType(int position) {
		return VIEWTYPE_NORMAL; //默认返回1  普通holder
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//根视图
		BaseHolder<T> baseHolder = null;
		if (convertView == null) {
			//判断当前条目是普通还是加载更多
			if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
				//返回加载更多的holder
				baseHolder = (BaseHolder<T>) getLoadMoreHolder();
			} else {
				//返回普通的holder
				baseHolder = getSpecialHolder(position);
			}
		} else {
			//有缓存
			baseHolder = (BaseHolder<T>) convertView.getTag();
		}

		//数据和视图绑定 要分类型
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			if (hasLoadMore()) {
				//加载更多开关为开   触发 加载更多数据
				triggerLoadMoreData();
			} else {
				//没有更多数据 
				mLoadMoreHolder.refreshHolderView(LoadMoreHolder.LOADMORE_NONE);
			}
		} else {
			//展示普通视图
			//获取数据
			T data = mDatas.get(position);
			//把数据设置给holderview 用来绑定数据和view
			baseHolder.setDataToRefreshHolderView(data);
		}
		return baseHolder.mHolderView;
	}

	/**
	 * @desc 触发加载更多数据的方法
	 */
	private void triggerLoadMoreData() {
		if (mLoadMoreTask == null) {
			//没有正在加载更多才去加载更多  避免频繁触发加载更多

			//初始化mLoadMoreHolder视图为加载中
			int state = mLoadMoreHolder.LOADMORE_LOADING;
			mLoadMoreHolder.setDataToRefreshHolderView(state);

			mLoadMoreTask = new LoadMoreTask();
			ThreadPoolExecutorProxyFactory.getNormalThreadPoolExecutorProxy().execute(mLoadMoreTask);
		}
	}

	//加载更多数据的任务
	class LoadMoreTask implements Runnable {
		private static final int	PAGESIZE	= 20;	//默认每页20条数据

		@Override
		public void run() {
			int state; //完成加载返回的状态值
			List<T> loadMoreData = null; //加载到的更多数据

			//真正的子线程加载更多数据
			try {
				loadMoreData = onLoadMore();
				if (loadMoreData == null || loadMoreData.size() < PAGESIZE) {
					//没有加载到数据或者加载到的数据 小于20条（最后一页）  状态为没有更多数据
					state = LoadMoreHolder.LOADMORE_NONE;
				} else {
					//想	20条 加载到了20条  可能还会有更多数据
					state = LoadMoreHolder.LOADMORE_LOADING;
				}
			} catch (Exception e) {
				//加载更多数据时出错  状态为加载失败
				state = LoadMoreHolder.LOADMORE_RETRY;
				e.printStackTrace();
			}

			/*-------- 定义两个临时变量   用于  下面内部类访问的外部变量必须为final ----------*/
			final int tempState = state;
			final List<T> tempLoadMoreData = loadMoreData;

			/*-------- 加载完成 根据数据   主线程刷新ui ---------*/
			UIUtils.postTaskSafely(new Runnable() {
				@Override
				public void run() {
					//1.更新数listview据集，notifyDataSetChange方法刷新界面(希望完成完成加载更多之后返回一个加载到的更多数据的list集合)
					if (tempLoadMoreData != null) {
						//加载到了更多数据
						mDatas.addAll(tempLoadMoreData); //更新数据集 
						notifyDataSetChanged(); //刷新界面
					}

					//2.根据state刷新mLoadMoreHolder视图显示，（希望完成加载更多之后返回一个int状态值图）
					mLoadMoreHolder.setDataToRefreshHolderView(tempState);
				}
			});

			//置空当前LoadMoreTask 标识当前任务执行完了
			mLoadMoreTask = null;
		}
	}

	/**
	 * @desc loadMore的开关，是否有加载更多   子类需要复写该方法 修改返回值
	 * 		   如果子类不复写，默认返回true，有更多数据
	 */
	protected boolean hasLoadMore() {
		return true;
	}

	/**
	 * @desc    真正子线程中加载更多数据，默认返回null，子类可以选择性的复写，返回具体的加载到的更多数据
	 * @desc    加载更多数据时，可能出现问题，需要抛出异常，把异常throws抛给调用处
	 * @return  返回加载到的更多数据的list集合
	 */
	public List<T> onLoadMore() throws Exception {
		return null;
	}

	/**
	 * @desc 返回加载更多的视图
	 */
	private LoadMoreHolder getLoadMoreHolder() {
		if (mLoadMoreHolder == null) {
			mLoadMoreHolder = new LoadMoreHolder();
		}
		return mLoadMoreHolder;
	}

	/**
	 * @param position 
	 * @desc 返回一个BaseHolder的子类  子类具体实现
	 */
	protected abstract BaseHolder<T> getSpecialHolder(int position);

	/**
	 * @desc 条目单击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//position要减去头
		if (mAbsListView instanceof ListView) {
			position = position - ((ListView) mAbsListView).getHeaderViewsCount();
		}
		
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			//当前item是loadMore 单击重新加载
			triggerLoadMoreData();
		} else {
			//普通条目  子类自己实现
			onNormalItemClick(parent, view, position, id);
		}
	}

	/**
	 * @desc 普通条目单击事件 子类自己实现
	 */
	protected void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
}
