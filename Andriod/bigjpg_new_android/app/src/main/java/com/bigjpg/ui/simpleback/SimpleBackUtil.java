package com.bigjpg.ui.simpleback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

/**
 * @author Momo
 * @date 2019-01-30 17:07
 */
public class SimpleBackUtil {

    public static void show(Context context, SimpleBackPage page) {
        show(context, page, null);
    }

    public static void show(Context context, SimpleBackPage page, Bundle args) {
        Intent intent = createLaunchIntent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        if (args != null) {
            intent.putExtra(SimpleBackActivity.KEY_ARGS, args);
        }
        context.startActivity(intent);
    }

    public static void showForResult(Fragment fragment, int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(Fragment fragment, int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.KEY_ARGS, args);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(Activity context, int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.KEY_ARGS, args);
        context.startActivityForResult(intent, requestCode);
    }

    public static void showForResult(Activity context, int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        context.startActivityForResult(intent, requestCode);
    }

    public static void show(Context context, SimpleBackPage page, int flag) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.KEY_PAGE, page.getValue());
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    private static Intent createLaunchIntent(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

}
