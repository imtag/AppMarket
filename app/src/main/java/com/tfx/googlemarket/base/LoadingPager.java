package com.tfx.googlemarket.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.factory.ThreadPoolExecutorProxyFactory;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-7
 * @desc      常用加载页面封装 负责视图显示 数据加载

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: LoadingPager.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class LoadingPager extends FrameLayout {

	private View			mLoadingView;
	private View			mErrorView;
	private View			mEmptyView;
	private View			mSuccessView;

	public static final int	STATE_LOADING	= 0;				// 加载中
	public static final int	STATE_ERROR		= 1;				// 错误
	public static final int	STATE_EMPTY		= 2;				// 空
	public static final int	STATE_SUCCESS	= 3;				// 成功
	public static final int	STATE_NONE		= 4;				// 初始化

	public int				mCurrentState	= STATE_NONE;	// 默认状态

	public LoadingPager(Context context) {
		super(context);
		initCommonViews();
	}

	/**
	* 任何应用其实就只有4种页面类型
	*	 ① 加载页面
	*	 ② 错误页面
	*	 ③ 空页面              
	*	 ④ 成功页面     
	*	     ①②③三种页面一个应用基本是固定的
	*	     每一个fragment对应的页面④就不一样
	*	     进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个       
	*/
	private void initCommonViews() {
		//初始化三个页面 && 添加到容器中

		// ① 加载页面
		mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
		this.addView(mLoadingView);

		// ② 错误页面
		mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
		this.addView(mErrorView);

		// ③ 空页面
		mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
		this.addView(mEmptyView);

		//根据当前状态更新界面显示
		RefreshUiByState();
	}

	/**
	 * @desc 根据状态选择相应视图
	 * @call 更新界面
	 */
	private void RefreshUiByState() {
		// 控制加载中视图显示/隐藏
		mLoadingView.setVisibility((mCurrentState == STATE_LOADING) || (mCurrentState == STATE_NONE) ? 0 : 8); // 0:Visivle 8:Gone

		// 控制错误视图显示/隐藏
		mErrorView.setVisibility((mCurrentState == STATE_ERROR) ? 0 : 8);

		//错误页面 重新加载数据单击事件
		mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//重新触发加载数据
				triggerLoadData();
			}
		});

		// 控制空白视图显示/隐藏
		mEmptyView.setVisibility((mCurrentState == STATE_EMPTY) ? 0 : 8);

		//数据加载成功了 && 成功视图还没有
		if (mCurrentState == STATE_SUCCESS && mSuccessView == null) {
			//初始化成功视图 将视图添加到fl
			mSuccessView = initSuccessView();
			this.addView(mSuccessView);
		}

		// 控制成功视图显示/隐藏
		if (mSuccessView != null) {
			mSuccessView.setVisibility((mCurrentState == STATE_SUCCESS) ? 0 : 8);
		}
	}

	/**
	 * 加载数据的方法
	 *     数据加载的流程
	 *  	  ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
	 *  	  ② 异步加载数据  -->显示加载视图
	 *  	  ③ 处理加载结果
	 *  		① 成功-->显示成功视图
	 *  		② 失败
	 *  			① 数据为空-->显示空视图
	 *  			② 数据加载失败-->显示加载失败的视图
	 */ 
	public void triggerLoadData() {
		//当前状态不是成功 && 不是正在加载中
		if(mCurrentState != STATE_SUCCESS && mCurrentState != STATE_LOADING){
			//加载之前  重置当前状态
			mCurrentState = STATE_LOADING;
			RefreshUiByState();
			
			//② 异步加载数据 
//			new Thread(new LoadDataTask()).start();
			//线程池执行加载数据的任务
			ThreadPoolExecutorProxyFactory.getNormalThreadPoolExecutorProxy().execute(new LoadDataTask());
		}
	}

	class LoadDataTask implements Runnable {
		@Override
		public void run() {
			//真正开始加载数据  加载成功返回界面状态值
			LoadedResult loadedResult = initData();
			mCurrentState = loadedResult.state;

			//根据状态  更新界面  主线程
			UIUtils.postTaskSafely(new Runnable() {
				@Override
				public void run() {
					RefreshUiByState();
				}
			});
		}
	}

	/**
	 * @desc 子类具体实现  完成成功视图初始化
	 * @call 数据加载成功 更新界面
	 */
	protected abstract View initSuccessView();

	/**
	 * @desc 子类具体实现  完成数据加载
	 * @call 调用triggerLoadData
	 */
	protected abstract LoadedResult initData();

	//状态的枚举 限制结果状态值
	public enum LoadedResult {
		SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR); //给枚举赋值
		int	state;

		//枚举构造器 
		LoadedResult(int state) {
			this.state = state;
		}
	}
}
