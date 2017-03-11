package com.tfx.googlemarket.base;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tfx.googlemarket.base.LoadingPager.LoadedResult;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-7
 * @desc      fragment基类  

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BaseFragment.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class BaseFragment extends Fragment {
	private LoadingPager	mLoadingPager;
	
	/**
	 * @desc 获取LoadingPager的方法
	 * @call
	 */
	public LoadingPager getLoadingPager() {
		return mLoadingPager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//实例化LoadingPager 返回展示
		if(mLoadingPager == null){ //第一次进来时走
			mLoadingPager = new LoadingPager(UIUtils.getContext()) {
				@Override
				protected View initSuccessView() {
					//让具体子类去实现
					return BaseFragment.this.initSuccessView();
				}
				
				@Override
				protected LoadedResult initData() {
					//让具体子类去实现
					return BaseFragment.this.initData();
				}
			};
		}else{
			//第二次进来时走  
			//因为第一次进来已经将mLoadingPager添加到viewgroup中，所以第二次执行时要将第一次添加的mLoadingPager移除   再return mLoadingPager，添加到viewgroup
			ViewGroup parent = (ViewGroup) mLoadingPager.getParent();
			if(parent != null && parent instanceof ViewGroup){
				parent.removeView(mLoadingPager);
			}
		}
		return mLoadingPager; //添加到viewgroup
	}

	protected abstract LoadedResult initData();

	protected abstract View initSuccessView();
	
	/**
	 * @desc 数据非空校验
	 * @desc result请求回来的数据
	 * @return 不同的LoadedResult状态值
	 */
	public LoadedResult checkState(Object result){
		//数据为空
		if(result == null){
			return LoadedResult.EMPTY;
		}
		
		//数据是list类型 大小为0 返回空
		if(result instanceof List){
			if(((List) result).size() == 0){
				return LoadedResult.EMPTY;
			}
		}
		
		//数据是map类型 大小为0 返回空
		if(result instanceof Map){
			if(((Map) result).size() == 0){
				return LoadedResult.EMPTY;
			}
		}
		
		//否则返回成功
		return LoadedResult.SUCCESS;
	}
}
