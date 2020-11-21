package com.bigjpg.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.AnimRes;
import androidx.annotation.AnyRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;

import java.lang.reflect.Field;


/**
 * ResourcesUtil
 *
 * @author Momo
 * @date 2016-03-30 18:47
 */
public class ResourcesUtil {

    private static Context getContext() {
        return BigJPGApplication.getInstance();
    }

    public static int dp(Context context, int dp){
        return AppUtil.dipToPx(context, dp);
    }

    /**
     * 布局有theme相关属性 时使用
     * @param context
     * @param resId
     * @return
     */
    public static View inflate(Context context, @LayoutRes int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 布局有theme相关属性 时使用
     * @param context
     * @param resId
     * @param viewGroup
     * @param attachToRoot
     * @return
     */
    public static View inflate(Context context, @LayoutRes int resId, ViewGroup viewGroup, boolean attachToRoot) {
        return LayoutInflater.from(context).inflate(resId, viewGroup, attachToRoot);
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static String getString(Context context, @StringRes int resId) {
        return getResources(context).getString(resId);
    }

    public static String getString(Context context, @StringRes int resId, Object... formatArgs){
        return getResources(context).getString(resId, formatArgs);
    }

    public static String[] getStringArray(Context context, @ArrayRes int resId) {
        return getResources(context).getStringArray(resId);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int resId) {
        return getResources(context).getDimensionPixelSize(resId);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    public static int getColor(Context context, @ColorRes int resId) {
        return getResources(context).getColor(resId);
    }

    public static int getInteger(Context context, @IntegerRes int resId){
        return getResources(context).getInteger(resId);
    }

    public static Configuration getConfiguration(Context context){
        return getResources(context).getConfiguration();
    }

    public static Animation loadAnimation(@AnimRes int resId){
        return AnimationUtils.loadAnimation(getContext(), resId);
    }

    /**
     * 通过drawable名来获取drawable的id
     *
     * @return
     */
    public static int getDrawableIdByName(String name) {
        try {
            Class<?> drawableClazz = R.drawable.class;
            Field field = drawableClazz.getField(name);
            int drawableId = field.getInt(new R.drawable());
            return drawableId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static StateListDrawable createBackgroundColorSelector(Context context, @ColorInt int normalColor, @ColorInt int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        states.addState(new int[]{}, new ColorDrawable(normalColor));
        return states;
    }

    public static ColorStateList createTextColorSelector(Context context, @ColorInt int normalColor, @ColorInt int pressedColor) {
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{}}, new int[]{pressedColor, normalColor});
        return colorStateList;
    }

    public static StateListDrawable createBackgroundDrawableSelector(Drawable normal, Drawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        return states;
    }

    /**
     * 根据资源名称获取资源id
     * ("status_bar_height", "dimen", "android")
     * @param context
     * @param name
     * @param defType
     * @param defPackage
     * @return
     */
    public static int getResourceId(Context context, String name, String defType, String defPackage){
        Resources resources = context.getResources();
        return resources.getIdentifier(name, defType, defPackage);
    }

    /**
     * 根据资源id获取资源名称
     * @param context
     * @param resId
     * @return
     */
    public static String getResourceId(Context context, @AnyRes int resId){
        try{
            Resources resources = context.getResources();
            return resources.getResourceName(resId);
        }catch (Resources.NotFoundException e){
            return "";
        }
    }
}
