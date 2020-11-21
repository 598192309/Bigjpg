package com.bigjpg.ui.activity.photo.chooser;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.ui.base.BaseActivity;
import com.bigjpg.ui.listener.SimpleAnimationListener;
import com.bigjpg.util.AfterPermissionGranted;
import com.bigjpg.util.EasyPermissions;
import com.bigjpg.util.SDCardUtil;
import com.bigjpg.util.TextViewUtil;
import com.bigjpg.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片选择主界面，列出所有图片文件夹
 */
public class ImageChooserActivity extends BaseActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks, ImageChooseGroupAdapter.OnImageGroupClickListener, ImageListAdapter.OnImageCheckedListener {

    @BindView(R.id.grid_view)
    GridView mImagesGv;
    @BindView(R.id.group_list)
    ListView mGroupListView;
    @BindView(R.id.done)
    TextView mTvDone;
    @BindView(R.id.group_layout)
    FrameLayout mGroupLayout;
    @BindView(R.id.group_name)
    TextView mTvGroupName;
    @BindView(R.id.group_shadow)
    View mGroupShadow;

    private ImageListAdapter mImageAdapter;
    private ArrayList<ImageListBundle> mImages = new ArrayList<>();
    private ImageChooseGroupAdapter mGroupAdapter;
    private List<ImageGroup> mGroupData = new ArrayList<>();
    private List<ImageListBundle> mChooseImages;
    private ImageLoadTask mLoadTask;
    private boolean mGifEnable;
    private int mMaxChoose = 10;
    private int mCurrentGroupPosition = 0;
    private boolean mEnableMultiChoose;
    private boolean mIsHidingGroupList;
    private boolean mIsDataEmpty;
    private String mConfirmText;

    private static final int RC_EXTERNAL_STORAGE = 11;
    private final String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isRecreated()){
            finish();
            return;
        }

        mEnableMultiChoose = mMaxChoose > 1;
        if (mEnableMultiChoose) {
            mChooseImages = new ArrayList<>();
        }

        mConfirmText = getString(R.string.ok);
        ViewUtil.setViewVisible(mTvDone, mEnableMultiChoose);
        TextViewUtil.setText(mTvDone, mConfirmText);
        setTitle(R.string.image);
        if (EasyPermissions.hasPermissions(this, mPermissions)) {
            loadImages();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_tip), RC_EXTERNAL_STORAGE, mPermissions);
        }
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_image_chooser, container, false);
    }

    @Override
    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_titlebar_timeline_allow_see, container, false);
    }

    @Override
    protected Presenter onCreatePresenter() {
        return null;
    }

    @OnClick(R.id.group_name)
    void onGroupNameClick() {
        if(mIsDataEmpty){
            return;
        }
        if(ViewUtil.isGentleFastDoubleClick()){
            return;
        }
        if (ViewUtil.isVisible(mGroupLayout)) {
            hideImageGroup();
        } else {
            showImageGroup();
        }
    }

    @OnClick(R.id.group_layout)
    void onGroupLayoutClick() {
        if(ViewUtil.isGentleFastDoubleClick()){
            return;
        }
        hideImageGroup();
    }

    @Override
    public void onBackPressed() {
        if (mIsHidingGroupList) {
            mIsHidingGroupList = false;
            return;
        }
        if (ViewUtil.isVisible(mGroupLayout)) {
            hideImageGroup();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        if (!SDCardUtil.hasSDCard()) {
            showToast(R.string.no_sdcard);
            return;
        }

        // 线程正在执行
        if (mLoadTask != null && mLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        showLoadingDialog(R.string.loading);
        mLoadTask = new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    setImageGroupAdapter((ArrayList<ImageGroup>) result);
                } else {
                    // 加载失败，显示错误提示
                    showToast(R.string.error);
                }
                hideLoadingDialog();
            }
        }, mGifEnable);
        TaskUtil.execute(mLoadTask);
    }

    private void setImageGroupAdapter(ArrayList<ImageGroup> data) {
        if (data == null || data.size() == 0) {
            mIsDataEmpty = true;
            showMessageDialog(R.string.no_image);
            return;
        }

        mIsDataEmpty = false;
        mGroupData.clear();
        mGroupData.addAll(data);
        ImageGroup group = data.get(0);
        group.setChecked(true);
        setImageGridAdapter(group);
        mGroupAdapter = new ImageChooseGroupAdapter(this, mGroupData);
        mGroupListView.setAdapter(mGroupAdapter);
        mGroupAdapter.setOnImageGroupClickListener(this);
    }

    private void setImageGridAdapter(ImageGroup group) {
        mImages.clear();
        TextViewUtil.setText(mTvGroupName, group.getDirName());
        if (group.getImages().size() == 0) {
            showMessageDialog(R.string.no_image);
            return;
        }
        mImages.addAll(group.getImages());
        if (mImageAdapter == null) {
            mImageAdapter = new ImageListAdapter(this, mImages, mEnableMultiChoose);
            mImageAdapter.setOnImageCheckedListener(this);
            mImagesGv.setAdapter(mImageAdapter);
            if (!mEnableMultiChoose) {
                mImagesGv.setOnItemClickListener(this);
            }
        } else {
            mImageAdapter.notifyDataSetChanged();
            mImagesGv.post(new Runnable() {
                @Override
                public void run() {
                    mImagesGv.setSelection(0);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        ImageListBundle data = mImages.get(position);
        String path = data.imagePath;
        Intent intent = new Intent();
        intent.putExtra(ImageConstants.EXTRA_PATH, path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == ImageConstants.REQUEST_CODE_GALLERY_THIRD) {
            Intent intent = new Intent();
            intent.putExtra(ImageConstants.EXTRA_PATH, data.getStringExtra(ImageConstants.EXTRA_PATH));
            intent.putStringArrayListExtra(ImageConstants.EXTRA_PATH_LIST, data.getStringArrayListExtra(ImageConstants.EXTRA_PATH_LIST));
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (requestCode == RC_EXTERNAL_STORAGE) {
            if (EasyPermissions.hasPermissions(this, mPermissions)) {
                loadImages();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void onExternalPermissionGranted() {
        loadImages();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.permission_tip), R.string.ok, R.string.cancel, null, perms);
    }

    private void showImageGroup() {
        if (mIsHidingGroupList) {
            mIsHidingGroupList = false;
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
        animation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                if(mGroupLayout != null){
                    mGroupLayout.clearAnimation();
                }
                if(mGroupShadow != null){
                    ViewUtil.setViewVisible(mGroupShadow, true);
                }
            }
        });
        ViewUtil.setViewVisible(mGroupLayout, true);
        mGroupLayout.startAnimation(animation);
    }

    private void hideImageGroup() {
        mIsHidingGroupList = true;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_bottom);
        animation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                ViewUtil.setViewVisible(mGroupShadow, false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                if(mGroupLayout != null){
                    mGroupLayout.clearAnimation();
                    ViewUtil.setViewVisible(mGroupLayout, false);
                }
                mIsHidingGroupList = false;
            }
        });
        mGroupLayout.startAnimation(animation);
    }

    @Override
    public void onImageCheckChanged(ImageListAdapter.ViewHolder viewHolder, ImageListBundle data, int position) {
        if (data.isChecked) {
            data.isChecked = false;
            ViewUtil.setViewVisible(viewHolder.overlay, false);
            viewHolder.checkBox.setBackgroundResource(R.drawable.ic_uncheck);
            mChooseImages.remove(data);
        } else {
            if (mChooseImages.size() > mMaxChoose - 1) {
                showMessageDialog(getString(R.string.max_choose_images, mMaxChoose));
                ViewUtil.setViewVisible(viewHolder.overlay, false);
            } else {
                ViewUtil.setViewVisible(viewHolder.overlay, true);
                viewHolder.checkBox.setBackgroundResource(R.drawable.ic_check);
                data.isChecked = true;
                mChooseImages.add(data);
            }
        }

        int chooseSize = mChooseImages.size();
        if (chooseSize > 0) {
            mTvDone.setEnabled(true);
            mTvDone.setText(String.format("%s(%s/%s)", mConfirmText, chooseSize, mMaxChoose));
        } else {
            mTvDone.setEnabled(false);
            mTvDone.setText(mConfirmText);
        }
    }

    @Override
    public void onImageGroupClick(int position) {
        if (mCurrentGroupPosition != position) {
            ImageGroup group = mGroupAdapter.getItem(position);
            group.setChecked(true);
            setImageGridAdapter(group);
            for (ImageGroup item : mGroupData) {
                if (item != group && item.isChecked()) {
                    item.setChecked(false);
                    mGroupAdapter.notifyDataSetChanged();
                    break;
                }
            }
            mCurrentGroupPosition = position;
        }
        hideImageGroup();
    }

    @OnClick(R.id.done)
    void onDoneClick() {
        ArrayList<String> paths = new ArrayList<>();
        for (ImageListBundle bundle : mChooseImages) {
            paths.add(bundle.imagePath);
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImageConstants.EXTRA_PATH_LIST, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
