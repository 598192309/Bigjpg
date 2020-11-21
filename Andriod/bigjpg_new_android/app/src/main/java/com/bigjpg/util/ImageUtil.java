package com.bigjpg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;

import com.bigjpg.exception.ChoosePictureException;

import java.io.File;

public class ImageUtil {

    public static final int REQUEST_CODE_GALLERY_KITKAT = 0x21;
    public static final int REQUEST_CODE_GALLERY = 0x22;
    public static final String FILE_PROVIDER_AUTHORITY = "com.bigjpg.fileProvider";

    public static Uri getImageUri(Context context, String path) {
        Uri imageUri;
        if (Build.VERSION.SDK_INT > 23) {
            imageUri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, new File(path));
        } else {
            imageUri = Uri.fromFile(new File(path));
        }
        return imageUri;
    }

    /**
     * 调用系统选择图片
     * (可能会ActivityNotFoundException)
     */
    public static void chooseImageForResult(Object object, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 调用系统选择图片
     * (可能会ActivityNotFoundException)
     */
    public static void chooseImageForResultV19(Object object, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, null);
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 选择图片返回处理
     *
     * @param data
     * @return
     */
    public static Uri onActivityResult(Context context, int requestCode, Intent data) throws ChoosePictureException {
        Uri originalUri = null;
        if (data == null) {
            return null;
        }
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                originalUri = data.getData();
                break;
            case REQUEST_CODE_GALLERY_KITKAT:
                originalUri = data.getData();
                String originPath = UriUtil.getPath(context, originalUri);
                if (StringUtil.isEmpty(originPath)) {
                    throw new ChoosePictureException();
                }
                break;
            default:
                break;
        }
        return originalUri;
    }
}
