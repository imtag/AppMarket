package com.tfx.googlemarket.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.tfx.googlemarket.utils.UIUtils;

public class ListViewFactory {
	public static ListView createListView() {
		ListView lv = new ListView(UIUtils.getContext());
		lv.setFastScrollEnabled(true);
		lv.setCacheColorHint(Color.TRANSPARENT); 
		lv.setSelector(new ColorDrawable(Color.TRANSPARENT)); //选中颜色
		return lv;
	}
}
