package com.tfx.googlemarket.bean;

import java.util.List;

public class AppInfoBean {
	public String		des;			//应用描述
	public String		downloadUrl;	//应用下载地址
	public String		iconUrl;		//应用图片地址
	public long			id;			//应用id
	public String		name;			//应用名
	public String		packageName;	//应用包名
	public long			size;			//应用大小
	public float		stars;			//应用评分

	/* 详细页面 */
	public String		author;		//              黑马程序员
	public String		date;			//              2015-06-10
	public String		downloadNum;	//        40万+
	public String		version;		//                  1.1.0605.0

	public List<String>	screen;		//                      Array
	public List<Safe>	safe;			//            Array

	public class Safe {
		public String	safeDes;		//               已通过安智市场安全检测，请放心使用
		public int		safeDesColor;	//              app/com.itheima.www/safeDesUrl0.jpg
		public String	safeUrl;		//         app/com.itheima.www/safeIcon0.jpg
		public String	safeDesUrl;	//	app/com.itheima.www/safeDesUrl0.jpg
	}

}
