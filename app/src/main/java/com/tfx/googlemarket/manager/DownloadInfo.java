package com.tfx.googlemarket.manager;

/**
 * @author    Tfx
 * @comp      GOD
 * @date      2016-10-19
 * @desc      下载需要的数据

 * @version   $Rev$
 * @auther    $Author$
 * @date      $Date$
 * @id        $Id$
 */

public class DownloadInfo {
	public String	savePath;									// 保存路径
	public String	saveName;									//保存文件名
	public String	downloadUrl;								//下载路径
	public String	packageName;								//包名
	public int		state	= DownloadManager.STATE_UNDOWNLOAD; //默认未下载
	public long		maxProgress;
	public long		curProgress;
	public Runnable	task;										//绑定task到身上
}
