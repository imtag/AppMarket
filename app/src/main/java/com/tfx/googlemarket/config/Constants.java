package com.tfx.googlemarket.config;

public class Constants {
    //使用类进行分类
    public static final class URLS {
        public static final String BASEURL = "http://10.0.3.2:8080/GooglePlayServer/"; //基本url
        public static final String IMAGEBASEURL = BASEURL + "image?name=";                    //图片基本url
        public static final String DOWNLOADURL = BASEURL + "download";
    }

    public static final long PROTOCOLTIMEOUT = 1000 * 60 * 5;    //本地硬盘缓存数据   5分钟过期
}
