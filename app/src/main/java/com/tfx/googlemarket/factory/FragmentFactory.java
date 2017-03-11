package com.tfx.googlemarket.factory;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;

import com.tfx.googlemarket.base.BaseFragment;
import com.tfx.googlemarket.fragment.ApplyFragment;
import com.tfx.googlemarket.fragment.CategoryFragment;
import com.tfx.googlemarket.fragment.GameFragment;
import com.tfx.googlemarket.fragment.HomeFragment;
import com.tfx.googlemarket.fragment.RecommendFragment;
import com.tfx.googlemarket.fragment.SubjectFragment;
import com.tfx.googlemarket.fragment.HotFragment;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-6
 * @desc      创建fragent的工厂

 * @version   $Rev: 4 $
 * @auther    $Author: tfx $
 * @date      $Date: 2016-10-14 10:56:02 +0800 (星期五, 14 十月 2016) $
 * @id        $Id: FragmentFactory.java 4 2016-10-14 02:56:02Z tfx $
 */

/*	 <string-array name="main_titles">
	     <item>首页</item>
	     <item>应用</item>
	     <item>游戏</item>
	     <item>专题</item>
	     <item>推荐</item>
	     <item>分类</item>
	     <item>排行</item>
 	 </string-array>
 */
public class FragmentFactory extends Fragment {
	
	private static Map<Integer,BaseFragment> mBaseFragments = new HashMap<Integer,BaseFragment>();
	
	private static final int HOME        = 0;
	private static final int APPLY       = 1;
	private static final int GAME        = 2;
	private static final int SUBJECT     = 3;
	private static final int RECOMMEND   = 4;
	private static final int CATEGORY    = 5;
	private static final int TOP         = 6;
	
	public static BaseFragment createFragment(int index) {
		//根据当前需要初始化的fragment的position 返回相应fragment
		
		BaseFragment fragment = null;
		
		//先从map中取
		if(mBaseFragments.containsKey(index)){
			fragment = mBaseFragments.get(index);
			return fragment;
		}
		
		switch (index) {
		case HOME:
			fragment = new HomeFragment();
			break;
		case APPLY:
			fragment = new ApplyFragment();
			break;
		case GAME:
			fragment = new GameFragment();
			break;
		case SUBJECT:
			fragment = new SubjectFragment();
			break;
		case RECOMMEND:
			fragment = new RecommendFragment();
			break;
		case CATEGORY:
			fragment = new CategoryFragment();
			break;
		case TOP:
			fragment = new HotFragment();
			break;

		default:
			break;
		}
		//将BaseFragment装入map集合
		mBaseFragments.put(index, fragment);
		return fragment;
	}
}
