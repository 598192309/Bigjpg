package com.bigjpg.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.bigjpg.model.constant.AppLanguages;

import java.util.HashMap;
import java.util.Locale;

public class AppLanguageUtils {

    private static HashMap<String, Locale> sAllLanguages = new HashMap<String, Locale>(6) {{
        put(AppLanguages.SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(AppLanguages.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
        put(AppLanguages.ENGLISH, Locale.ENGLISH);
        put(AppLanguages.JAPAN, Locale.JAPAN);
        put(AppLanguages.RU, new Locale("RU"));
        put(AppLanguages.TURKISH, new Locale("TR"));
    }};

    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        // app locale
        Locale locale = getLocaleByLanguage(context, newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    private static boolean isSupportLanguage(String language) {
        return sAllLanguages.containsKey(language);
    }

    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }

        return AppLanguages.ENGLISH;
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #sAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #sAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(Context context, String language) {
        if (isSupportLanguage(language)) {
            return sAllLanguages.get(language);
        } else {
            String defaultLanguage = getDefaultLanguage(context);
            for (String key : sAllLanguages.keySet()) {
                if (TextUtils.equals(key, defaultLanguage)) {
                    return sAllLanguages.get(key);
                }
            }
        }
        return Locale.ENGLISH;
    }

    public static String getDefaultLanguage(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = configuration.locale;
        if (locale != null) {
            String language = locale.getLanguage();
            if (language.endsWith("zh")) {
                if ("TW".equalsIgnoreCase(locale.getCountry())) {
                    return AppLanguages.TRADITIONAL_CHINESE;
                } else {
                    return AppLanguages.SIMPLIFIED_CHINESE;
                }
            } else if (language.endsWith("ja")) {
                return AppLanguages.JAPAN;
            } else if (language.endsWith("en")) {
                return AppLanguages.ENGLISH;
            } else if (language.endsWith("ru")) {
                return AppLanguages.RU;
            } else if (language.endsWith("zh_rTW")) {
                return AppLanguages.TRADITIONAL_CHINESE;
            } else if(language.endsWith("tr")){
                return AppLanguages.TURKISH;
            }else {
                return AppLanguages.ENGLISH;
            }
        }
        return AppLanguages.ENGLISH;
    }

    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = AppLanguageUtils.getLocaleByLanguage(context, language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}