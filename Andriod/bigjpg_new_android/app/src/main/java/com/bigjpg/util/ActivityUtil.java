package com.bigjpg.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class ActivityUtil {

    public static boolean isActivityExist(Context context, Intent intent){
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            return true;
        }else{
            return false;
        }
    }

}
