package com.bigjpg.ui.activity.photo.chooser;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;

import com.bigjpg.R;
import com.bigjpg.util.ResourcesUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 使用contentProvider扫描图片异步任务
 */
public class ImageLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值对
     */
    private ArrayList<ImageGroup> mGroupList = new ArrayList<>();

    private boolean mGifEnable;

    public ImageLoadTask(Context context) {
        this(context, null);
    }

    public ImageLoadTask(Context context, OnTaskResultListener listener) {
       this(context, listener, false);
    }

    public ImageLoadTask(Context context, OnTaskResultListener listener, boolean gifEnable) {
        super();
        mContext = context;
        result = mGroupList;
        mGifEnable = gifEnable;
        setOnResultListener(listener);
    }

    /*
     * (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Uri imageUri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        // 构建查询条件，且查询jpeg png gif的图片
        StringBuilder selection = new StringBuilder();
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");
        if(mGifEnable){
            selection.append(" or ");
            selection.append(Media.MIME_TYPE).append("=?");
        }

        Cursor cursor = null;
        try {
            // 初始化游标
            String[] selectionArgs;
            if(mGifEnable){
                selectionArgs =  new String[]{"image/jpeg", "image/png", "image/gif"};
            }else{
                selectionArgs =  new String[]{"image/jpeg", "image/png"};
            }
            cursor = contentResolver.query(imageUri, null, selection.toString(), selectionArgs, Media.DATE_MODIFIED + " desc");
            // 遍历结果
            if(cursor != null){
                ImageGroup allImages = new ImageGroup();
                while (cursor.moveToNext()) {
                    // 获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(Media.DATA));

                    // 获取该图片的所在文件夹的路径
                    File file = new File(path);
                    if(!file.exists()){
                        continue;
                    }

                    ImageListBundle imageListBundle = new ImageListBundle(path, false);
                    allImages.addImage(imageListBundle);

                    String parentName;
                    if (file.getParentFile() != null) {
                        parentName = file.getParentFile().getName();
                    } else {
                        parentName = file.getName();
                    }
                    // 构建一个imageGroup对象
                    ImageGroup item = new ImageGroup();
                    // 设置imageGroup的文件夹名称
                    item.setDirName(parentName);

                    // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                    int searchIdx = mGroupList.indexOf(item);
                    if (searchIdx >= 0) {
                        // 如果是，该组的图片数量+1
                        ImageGroup imageGroup = mGroupList.get(searchIdx);
                        imageGroup.addImage(imageListBundle);
                    } else {
                        // 否则，将该对象加入到groupList中
                        item.addImage(imageListBundle);
                        mGroupList.add(item);
                    }
                }
                if(allImages.getImageCount() > 0){
                    allImages.setDirName(ResourcesUtil.getString(mContext, R.string.all_image));
                    mGroupList.add(0, allImages);
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return true;
    }
}
