package com.bigjpg.util;

import android.os.Build;

/**
 * 描述:系统版本
 * 
 * @author mofx
 * @date 2015年1月30日 下午6:02:01
 */
public class SdkLevel {

	public static final int CUPCAKE = 3;     // API 3
	public static final int DONUT = 4;       // API 4
	public static final int ECLAIR = 5;      // API 5
	public static final int ECLAIR_0_1 = 6;  // API 6
	public static final int ECLAIR_MR1 = 7;  // API 7
	public static final int FROYO = 8;       // API 8
	public static final int GINGERBREAD = 9; // API 9
	public static final int GINGERBREAD_MR1 = 10;        // API 10
	public static final int HONEYCOMB = 11;              // API 11
	public static final int HONEYCOMB_MR2 = 13;          // API 13
	public static final int ICE_CREAM_SANDWICH = 14;     // API 14
	public static final int ICE_CREAM_SANDWICH_MR1 = 15; // API 15
	public static final int JELLY_BEAN = 16;             // API 16
	public static final int JELLY_BEAN_MR1 = 17;         // API 17
	public static final int JELLY_BEAN_MR2 = 18; 
	public static final int KITKAT = 19; // Android 4.4
	public static final int LOLLIPOP = 21;
	public static final int M = 23;
	public static final int N = 24;
	public static final int N_MR1 = 25;
	public static final int O = 26; // Android 8.0
	public static final int P = 28; // Android 9.0
	public static final int Q = 29; // Android 10

	
	/**获取系统版本Level*/
	public static int getLevel() {
		return Build.VERSION.SDK_INT;
	}

	public static boolean isSupport(int level){
		return getLevel() >= level;
	}

	public static boolean equals(int level){
		return getLevel() == level;
	}

	public static boolean isSupportAndroidQ(){
		return getLevel() >= Q;
	}

}
