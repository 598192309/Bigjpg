package com.bigjpg.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.bigjpg.R;
import com.bigjpg.model.constant.EnlargeKey;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.ui.base.BaseActivity;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;
import com.bigjpg.util.UserManager;
import com.bigjpg.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class EnlargeConfigActivity extends BaseActivity {

    @BindView(R.id.config_type_carton)
    RadioButton mCarton;
    @BindView(R.id.config_type_photo)
    RadioButton mPhoto;
    @BindView(R.id.config_x2)
    RadioButton mX2;
    @BindView(R.id.config_x4)
    RadioButton mX4;
    @BindView(R.id.config_x8)
    RadioButton mX8;
    @BindView(R.id.config_x16)
    RadioButton mX16;
    @BindView(R.id.config_noise_none)
    RadioButton mNoiseNone;
    @BindView(R.id.config_noise_low)
    RadioButton mNoiseLow;
    @BindView(R.id.config_noise_medium)
    RadioButton mNoiseMedium;
    @BindView(R.id.config_noise_high)
    RadioButton mNoiseHigh;
    @BindView(R.id.config_noise_highest)
    RadioButton mNoiseHighest;

    @BindView(R.id.config_upgrade)
    View mUpgradeLayout;

    private EnlargeConfig mConfig;
    private static EnlargeConfig sTempConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.configure);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(EnlargeKey.ENLARGE_CONFIG)) {
            mConfig = (EnlargeConfig) bundle.getSerializable(EnlargeKey.ENLARGE_CONFIG);
        } else {
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        if (UserManager.getInstance().isLogin()) {
            if (UserManager.getInstance().isNeedUpgradeVersion()) {
                ViewUtil.setViewVisible(mUpgradeLayout, true);
                mX8.setEnabled(false);
                mX16.setEnabled(false);
            } else {
                mX8.setEnabled(true);
                mX16.setEnabled(true);
                ViewUtil.setViewVisible(mUpgradeLayout, false);
            }
        } else {
            mX8.setEnabled(false);
            mX16.setEnabled(false);
            ViewUtil.setViewVisible(mUpgradeLayout, true);
        }

        if (sTempConfig != null) {
            if (EnlargeConfig.Style.ART.equals(sTempConfig.style)) {
                mCarton.setChecked(true);
            } else {
                mPhoto.setChecked(true);
            }

            if (EnlargeConfig.X2.L2 == sTempConfig.x2) {
                mX2.setChecked(true);
            } else if (EnlargeConfig.X2.L4 == sTempConfig.x2) {
                mX4.setChecked(true);
            } else if (EnlargeConfig.X2.L8 == sTempConfig.x2) {
                if (mX8.isEnabled()) {
                    mX8.setChecked(true);
                } else {
                    mX2.setChecked(true);
                }
            } else if (EnlargeConfig.X2.L16 == sTempConfig.x2) {
                if (mX16.isEnabled()) {
                    mX16.setChecked(true);
                } else {
                    mX2.setChecked(true);
                }
            } else {
                mX2.setChecked(true);
            }

            if (EnlargeConfig.Noise.NONE == sTempConfig.noise) {
                mNoiseNone.setChecked(true);
            } else if (EnlargeConfig.Noise.LOW == sTempConfig.noise) {
                mNoiseLow.setChecked(true);
            } else if (EnlargeConfig.Noise.MEDIUM == sTempConfig.noise) {
                mNoiseMedium.setChecked(true);
            } else if (EnlargeConfig.Noise.HIGH == sTempConfig.noise) {
                mNoiseHigh.setChecked(true);
            } else if (EnlargeConfig.Noise.HIGHEST == sTempConfig.noise) {
                mNoiseHighest.setChecked(true);
            } else {
                mNoiseNone.setChecked(true);
            }
        }
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_enlarge_config, viewGroup, false);
    }

    @Override
    protected Presenter onCreatePresenter() {
        return null;
    }

    @OnClick(R.id.config_upgrade)
    void onUpgradeClick() {
        SimpleBackUtil.show(this, SimpleBackPage.UpgradeFragment);
    }

    @OnClick(R.id.config_confirm)
    void onConfirmClick() {
        if (mConfig != null) {
            Intent intent = new Intent();
            EnlargeConfig config = getConfig();
            setTempConfig(config);
            intent.putExtra(EnlargeKey.ENLARGE_CONFIG, config);
            intent.putExtra(EnlargeKey.ENLARGE_AFTER_ALL, false);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }


    @OnClick(R.id.config_enlarge_all)
    void onConfirmAllClick() {
        if (mConfig != null) {
            Intent intent = new Intent();
            EnlargeConfig config = getConfig();
            setTempConfig(config);
            intent.putExtra(EnlargeKey.ENLARGE_CONFIG, config);
            intent.putExtra(EnlargeKey.ENLARGE_AFTER_ALL, true);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    private EnlargeConfig getConfig() {
        if (mConfig != null) {
            mConfig.style = mCarton.isChecked() ? EnlargeConfig.Style.ART : EnlargeConfig.Style.PHOTO;
            if (mX2.isChecked()) {
                mConfig.x2 = EnlargeConfig.X2.L2;
            } else if (mX4.isChecked()) {
                mConfig.x2 = EnlargeConfig.X2.L4;
            } else if (mX8.isChecked()) {
                mConfig.x2 = EnlargeConfig.X2.L8;
            } else if (mX16.isChecked()) {
                mConfig.x2 = EnlargeConfig.X2.L16;
            } else {
                mConfig.x2 = EnlargeConfig.X2.L2;
            }

            if (mNoiseNone.isChecked()) {
                mConfig.noise = EnlargeConfig.Noise.NONE;
            } else if (mNoiseLow.isChecked()) {
                mConfig.noise = EnlargeConfig.Noise.LOW;
            } else if (mNoiseMedium.isChecked()) {
                mConfig.noise = EnlargeConfig.Noise.MEDIUM;
            } else if (mNoiseHigh.isChecked()) {
                mConfig.noise = EnlargeConfig.Noise.HIGH;
            } else if (mNoiseHighest.isChecked()) {
                mConfig.noise = EnlargeConfig.Noise.HIGHEST;
            } else {
                mConfig.noise = EnlargeConfig.Noise.NONE;
            }
        }
        return mConfig;
    }

    public void setTempConfig(EnlargeConfig config) {
        if (sTempConfig == null) {
            sTempConfig = new EnlargeConfig();
        }

        sTempConfig.style = config.style;
        sTempConfig.x2 = config.x2;
        sTempConfig.noise = config.noise;
    }

}
