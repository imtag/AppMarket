package com.tfx.googlemarket.holder;

import android.view.View;
import android.widget.TextView;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.utils.UIUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-9
 * @desc      提供视图，接收数据，数据和视图绑定

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: CopyOfHomeHolder.java 4 2016-10-14 02:56:02Z tfx $
 */

public class CopyOfHomeHolder {
	public View			mHolderView;	//根视图
	private String		mData;			//数据

	private TextView	mMTvTmp1;		//子孩子
	private TextView	mMTvTmp2;

	public CopyOfHomeHolder() {
		mHolderView = initHolderView();
		//根布局，找到一个合适的holder，把它绑定到自己身上
		//做holder需要的条件  持有根布局的孩子或者持有跟布局（findViewById找孩子）
		mHolderView.setTag(this); //HomeHolder持有两个textview子孩子，所以可以当做holder
	}

	/**
	 * @desc 初始化持有的视图，初始化持有的视图的子孩子
	 */
	private View initHolderView() {
		//1.初始化根布局
		View rootView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
		//2.初始化子孩子
		mMTvTmp1 = (TextView) rootView.findViewById(R.id.tmp_tv_1);
		mMTvTmp2 = (TextView) rootView.findViewById(R.id.tmp_tv_2);
		return rootView;
	}

	/**
	 * @desc 接收数据  用来绑定数据和视图
	 */
	public void setDataToHolderView(String data) {
		mData = data;
		refreshHolderView(data);
	}

	/**
	 * @desc 绑定数据和视图 显示holderview内容
	 */
	private void refreshHolderView(String data) {
		mMTvTmp1.setText("我是头 - " + data);
		mMTvTmp2.setText("我是尾 - " + data);
	}
}
