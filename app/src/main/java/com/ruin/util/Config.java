package com.ruin.util;

import java.io.File;

import android.os.Environment;
/**
 * 相关配置类
 * @author LHH
 *
 */

public class Config {
	//服务器地址
	public static String SERVER_IP = "192.168.1.1";
	
	//服务器端口 
	public static int SERVER_PORT = 6666;
	
	
	public static File pdir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LHH");
	//数据库目录
	 public  static final String DB_NAME = pdir.toString()+"/.ApLication/.Info.db";

}
