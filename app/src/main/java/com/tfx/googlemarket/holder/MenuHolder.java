package com.tfx.googlemarket.holder;

import android.view.View;

import com.tfx.googlemarket.R;
import com.tfx.googlemarket.base.BaseHolder;
import com.tfx.googlemarket.utils.UIUtils;

public class MenuHolder extends BaseHolder<Object> {

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
		return view;
	}

	@Override
	protected void refreshHolderView(Object data) {

	}

}
