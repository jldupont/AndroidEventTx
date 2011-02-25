package com.systemical.system;

import android.util.Log;

public class Debug {

	static String AppName=null;
	
	public static void setAppName(String _an) {
		AppName=_an;
	}
	
	public static void d(String msg) {
		Log.d(AppName, msg);
	}
	
	public static void e(String msg) {
		Log.e(AppName, msg);
	}
	
	public static void w(String msg) {
		Log.w(AppName, msg);
	}
	
	public static void wtf(String msg) {
		Log.wtf(AppName, msg);
	}

	public static void v(String msg) {
		Log.v(AppName, msg);
	}

	public static void i(String msg) {
		Log.i(AppName, msg);
	}
	
}///
