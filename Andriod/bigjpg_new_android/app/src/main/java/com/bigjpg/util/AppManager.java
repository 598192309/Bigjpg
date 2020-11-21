package com.bigjpg.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 描述:应用管理
 * 
 */
public final class AppManager {

	private static AppManager sAppManager;

	private List<Activity> mActivities = new ArrayList<Activity>();

	public static synchronized AppManager getInstance() {
		if (sAppManager == null) {
			sAppManager = new AppManager();
		}
		return sAppManager;
	}


	/**
	 * 退出软件
	 */
	public void exit() {
		List<Activity> items = new ArrayList<Activity>(mActivities);
		for (Activity item : items) {
			item.finish();
		}
	}

	/**
	 * 添加activity
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		boolean add = true;
		for (Activity item : mActivities) {
			if (item == activity) {
				add = false;
				break;
			}
		}
		if (add) {
			mActivities.add(activity);
		}
	}

	/**
	 * 删除当前activity
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		boolean remove = false;
		for (Activity item : mActivities) {
			if (item == activity) {
				remove = true;
				break;
			}
		}
		if (remove) {
			mActivities.remove(activity);
		}
	}
	
	/**
	 *  get current active activity
	 * @return
	 */
	public Activity getCurrentActivity(){
        int size = mActivities.size();
        if(size == 0){
            return null;
        }
		return mActivities.get(size - 1);
	}
	
	/**
	 * 获取某一个Activity
	 * @param position
	 * @return
	 */
	public Activity getActivity(int position){
		return mActivities.get(position);
	}
	
	/**
	 * 获取Activity的数量
	 * @return
	 */
	public int getActivitySize(){
		return mActivities.size();
	}

	public List<Activity> getActivities(){
		return mActivities;
	}
	
	/**清除非当前的Activity历史，保留当前Activity，以便Activity流畅过渡*/
	public void clearNotTopHistoryActivity() {
		int index = 0;
		int activitySize = getActivitySize();
		for (ListIterator<Activity> iterator = mActivities.listIterator(); iterator.hasNext(); index++) {
			Activity activity = iterator.next();
			if (activity != null && index != activitySize - 1) {
				activity.finish();
				iterator.remove();
			}
		}
	}


    public boolean isCurrentActivity(Activity activity){
    	 Activity current = getCurrentActivity();
    	 return (activity.equals(current));
	}
}
