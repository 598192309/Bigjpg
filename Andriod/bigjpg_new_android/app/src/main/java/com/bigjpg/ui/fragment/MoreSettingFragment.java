package com.bigjpg.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.application.BigJPGApplication;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.constant.AppLanguages;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;
import com.bigjpg.util.AppLanguageUtils;
import com.bigjpg.util.AppManager;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.LocalBroadcastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更多设置
 *
 * @author Momo
 * @date 2019-04-23 11:34
 */
public class MoreSettingFragment extends BaseFragment {

    @BindView(R.id.auto_download_cb)
    CheckBox mCbAutoDownload;
    @BindView(R.id.language_switch)
    TextView mTvLanguage;
    @BindView(R.id.night_mode)
    CheckBox mCbNightMode;

    @Override
    protected Presenter onCreatePresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCbAutoDownload.setChecked(AppPref.getInstance().isAutoDownloadImage());
        mCbAutoDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !AppUtil.hasRequiredPermission()) {
                    LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
                    mCbAutoDownload.setOnCheckedChangeListener(null);
                    mCbAutoDownload.setChecked(false);
                    mCbAutoDownload.setOnCheckedChangeListener(this);
                    showToast(R.string.permission_tip);
                    finishAttachedActivity();
                    return;
                }
                AppPref.getInstance().setAutoDownloadImage(isChecked);
            }
        });

        mCbNightMode.setChecked(BigJPGApplication.getInstance().isNightMode());
        mCbNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNightMode(isChecked);
            }
        });
    }

    private void setNightMode(boolean isNightMode) {
        if (isNightMode) {
            BigJPGApplication.getInstance().setNightMode(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            BigJPGApplication.getInstance().setNightMode(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SimpleBackUtil.show(getActivity(), SimpleBackPage.MoreSettingFragment);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishAttachedActivity();

        List<Activity> list = AppManager.getInstance().getActivities();
        for (Activity activity : list) {
            if (!activity.isFinishing()) {
                activity.recreate();
            }
        }
    }

    @OnClick(R.id.language_switch)
    void onLanguageClick() {
        List<String> lngs = new ArrayList<>();
        lngs.add("简体中文");
        lngs.add("繁體中文");
        lngs.add("日本語");
        lngs.add("English");
        lngs.add("Русский");
        lngs.add("Türkçe");
        DialogUtil.showListDialog(getActivity(), lngs, new DialogUtil.OnListDialogItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        setLocale(AppLanguages.SIMPLIFIED_CHINESE);
                        break;
                    case 1:
                        setLocale(AppLanguages.TRADITIONAL_CHINESE);
                        break;
                    case 2:
                        setLocale(AppLanguages.JAPAN);
                        break;
                    case 3:
                        setLocale(AppLanguages.ENGLISH);
                        break;
                    case 4:
                        setLocale(AppLanguages.RU);
                        break;
                    case 5:
                        setLocale(AppLanguages.TURKISH);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setLocale(String newLanguage) {
        AppPref.getInstance().setLanguage(newLanguage);
        AppLanguageUtils.changeAppLanguage(getActivity(), newLanguage);
        AppLanguageUtils.changeAppLanguage(BigJPGApplication.getInstance(), newLanguage);
        SimpleBackUtil.show(getActivity(), SimpleBackPage.MoreSettingFragment);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishAttachedActivity();

        List<Activity> list = AppManager.getInstance().getActivities();
        for (Activity activity : list) {
            if (!activity.isFinishing()) {
                activity.recreate();
            }
        }
    }

}
