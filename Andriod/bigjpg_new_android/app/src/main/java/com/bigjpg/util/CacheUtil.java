package com.bigjpg.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * CacheUtil
 *
 * @author Momo
 * @date 2019-01-14 11:12
 */
public class CacheUtil {

    public static void saveObject(Context context, Serializable object, String cacheKey) {
        ObjectOutputStream objOutput = null;
        try {
            objOutput = new ObjectOutputStream(context.openFileOutput(cacheKey, Context.MODE_PRIVATE));
            objOutput.writeObject(object);
            objOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objOutput != null)
                try {
                    objOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static <T> T getObject(Context context, String cacheKey) {
        ObjectInputStream objInput = null;
        File cacheFile = null;
        try {
            cacheFile = context.getFileStreamPath(cacheKey);
            if (cacheFile != null && !cacheFile.exists())
                return null;
            objInput = new ObjectInputStream(new FileInputStream(cacheFile));
            return (T) objInput.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            if (cacheFile != null && cacheFile.isFile())
                cacheFile.delete();
            return null;
        } finally {
            if (objInput != null) {
                try {
                    objInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class CacheKey {
        public static final String APP_CONFIG = "app_config";
        public static final String LOGIN_USER = "login_user";
    }
}
