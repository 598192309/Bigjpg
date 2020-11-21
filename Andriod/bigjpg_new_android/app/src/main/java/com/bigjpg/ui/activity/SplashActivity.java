package com.bigjpg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.ui.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * 启动页
 *
 * @author Momo
 * @date 2019-04-23 17:03
 */
public class SplashActivity extends BaseActivity {

    private static final int MSG_MAIN = 0x01;
    private Handler mHandler = new SplashHandler(this);

    private static class SplashHandler extends Handler {

        WeakReference<SplashActivity> splashActivityWeakReference;

        public SplashHandler(SplashActivity splashActivity) {
            splashActivityWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_MAIN && splashActivityWeakReference.get() != null) {
                SplashActivity splashActivity = splashActivityWeakReference.get();
                if (!splashActivity.isFinishing()) {
                    splashActivity.toMainActivity();
                }
            }
        }
    }

    @Override
    protected ViewGroup onCreateRootView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.activity_splash, null, false);
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected Presenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flags = getIntent().getFlags();
        if ((flags & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        mHandler.sendEmptyMessageDelayed(MSG_MAIN, 1200L);
    }

    private void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.keep, R.anim.keep);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
