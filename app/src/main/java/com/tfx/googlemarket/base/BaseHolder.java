package com.tfx.googlemarket.base;

import android.view.View;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-9
 * @desc     　 　holder基类
 * 			    提供视图，接收数据，数据和视图绑定

 * @version   $Rev: 4 $
 * @param <T>
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BaseHolder.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class BaseHolder<T> {
	public View	mHolderView;	//根视图　v
	private T	mData;			//数据  m             不知道具体数据类型  用泛型  调用时指定

	public BaseHolder() {
		//1.根视图初始化
		mHolderView = initHolderView();

		//根布局，找到一个合适的holder，把它绑定到自己身上
		//做holder需要的条件  持有根布局的孩子或者持有跟布局（findViewById找孩子）
		mHolderView.setTag(this); //HomeHolder持有两个textview子孩子，所以可以当做holder
	}

	/**
	 * @desc 初始化持有的视图，初始化持有的视图的子孩子
	 * 		  子类具体实现
	 */
	protected abstract View initHolderView();

	/**
	 * @desc 接收数据  用来绑定数据和视图
	 */
	public void setDataToRefreshHolderView(T data) {
		mData = data;
		refreshHolderView(data);
	}

	/**
	 * @desc 绑定数据和视图 显示holderview内容 
	 * 		  子类具体实现
	 */
	protected abstract void refreshHolderView(T data);
}
