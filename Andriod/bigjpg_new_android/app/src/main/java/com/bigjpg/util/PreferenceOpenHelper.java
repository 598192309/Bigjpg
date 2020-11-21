package com.bigjpg.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public abstract class PreferenceOpenHelper {

	private Context mContext;
	private String mName;
	private int mMode;

	public PreferenceOpenHelper(Context context, String prefName) {
		mContext = context;
		mName = prefName;
		mMode = Context.MODE_PRIVATE;
	}

	private SharedPreferences getSharedPreferences() {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(mName, mMode);
		return sharedPreferences;
	}

	public String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public Set<String> getStringSet(String key, Set<String> defValue){
		return getSharedPreferences().getStringSet(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return getSharedPreferences().getFloat(key, defValue);
	}

	public String getString(int resId, String defValue) {
		return getSharedPreferences().getString(mContext.getString(resId), defValue);
	}

	public int getInt(int resId, int defValue) {
		return getSharedPreferences().getInt(mContext.getString(resId), defValue);
	}

	public long getLong(int resId, long defValue) {
		return getSharedPreferences().getLong(mContext.getString(resId), defValue);
	}

	public boolean getBoolean(int resId, boolean defValue) {
		return getSharedPreferences().getBoolean(mContext.getString(resId), defValue);
	}

	public float getFloat(int resId, float defValue) {
		return getSharedPreferences().getFloat(mContext.getString(resId), defValue);
	}

	public SharedPreferences.Editor getEditor() {
		return getSharedPreferences().edit();
	}

	public void contains(String key) {
	    getSharedPreferences().contains(key);
	}

	public void removeKey(String key) {
		getEditor().remove(key).apply();
	}

	public void putString(String key, String value) {
		getEditor().putString(key, value).apply();
	}

	public void putStringSet(String key, Set<String> value){
		getEditor().putStringSet(key, value).apply();
	}

	public void putInt(String key, int value) {
		getEditor().putInt(key, value).apply();
	}

	public void putLong(String key, long value) {
		getEditor().putLong(key, value).apply();
	}

	public void putBoolean(String key, boolean value) {
		getEditor().putBoolean(key, value).apply();
	}

	public void putFloat(String key, float value) {
		getEditor().putFloat(key, value).apply();
	}
}
