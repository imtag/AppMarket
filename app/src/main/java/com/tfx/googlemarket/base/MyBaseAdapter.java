package com.tfx.googlemarket.base;

import java.util.List;

import android.widget.BaseAdapter;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-8
 * @desc      BaseAdapter基类

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: MyBaseAdapter.java 4 2016-10-14 02:56:02Z tfx $
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	private List<T>	mDatas;

	public MyBaseAdapter(List<T> datas) {
		this.mDatas = datas;
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
