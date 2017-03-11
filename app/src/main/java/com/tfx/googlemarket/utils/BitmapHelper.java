package com.tfx.googlemarket.utils;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-9
 * @desc      让bitmapUtils只实例化一次，形成单例

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: BitmapHelper.java 4 2016-10-14 02:56:02Z tfx $
 */

public class BitmapHelper {
	static BitmapUtils	bitmapUtils	= new BitmapUtils(UIUtils.getContext());

	public static <T extends View> void display(T container, String uri) {
		bitmapUtils.display(container, uri);
	}
}
