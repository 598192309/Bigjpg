package com.bigjpg.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 描述:SD卡工具类
 * 
 * @author mfx
 */
public class SDCardUtil {

	private SDCardUtil() {

	}

	/**
	 * 判断是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SDCard剩余大小
	 * <p>
	 * MB
	 * 
	 * @return
	 */
	public static long getAvailableSize() {
		if (hasSDCard()) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize / 1024 / 1024;
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 是否有足够的空间
	 * 
	 * @return
	 */
	public static boolean hasEnoughSpace() {
		return getAvailableSize() < 1024 * 1024 * 5;
	}

	/**
	 * 获取SDCard总容量大小
	 * <p>
	 * MB
	 * 
	 * @return
	 */
	public static long getTotalSize() {
		if (hasSDCard()) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				return totalBlocks * blockSize / 1024 / 1024;

			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 检测储存卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
