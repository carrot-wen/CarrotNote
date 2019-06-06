package com.carrot.carrotnote.util;

import android.util.Log;


public class LogUtil {
	
	private static String sTAG = "WEN";
	private static boolean sIsDebug = true;
	private static boolean sIsOpenLog = true;
	
	public static void setTAG(String tag) {
		sTAG = tag;
	}
	
	public static void setIsDebug(boolean isDebug) {
		sIsDebug = isDebug;
	}
	
	public static void i(String tag, String msg) {
		if(sIsOpenLog){			
			Log.i(sTAG, tag + " : " + msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if(sIsOpenLog){			
			Log.e(sTAG, tag + " : " + msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		if(sIsOpenLog){		
			Log.e(sTAG, msg, tr);
		}
	}
	
	public static void w(String tag, String msg) {
		if(sIsOpenLog){			
			Log.w(sTAG, tag + " : " + msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if(sIsOpenLog){	
			if (sIsDebug) {
				Log.d(sTAG, tag + " : " + msg);
			}
		}
	}

	public static void d(Throwable throwable, String... msg) {
		E(msg[0],throwable);
		for (int i = 1; msg.length > i ; i++) {
			d(msg[0],msg[i]);
		}
	}


	public static void E(String tag,Throwable e) {
		Log.e(sTAG, tag + ":  " + e.getMessage() + "\n\n");
		e.printStackTrace();
	}	
}
