package com.bigjpg.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bigjpg.R;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.constant.EnlargeKey;
import com.bigjpg.model.constant.EventType;
import com.bigjpg.model.entity.EnlargeConfig;
import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.response.AppConfigResponse;
import com.bigjpg.mvp.presenter.MainPresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.MainView;
import com.bigjpg.ui.adapter.FragmentMainPagerAdapter;
import com.bigjpg.ui.base.BaseActivity;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.ui.dialog.UpdateAppAlertDialog;
import com.bigjpg.ui.fragment.HistoryFragment;
import com.bigjpg.ui.fragment.HomeFragment;
import com.bigjpg.ui.fragment.SettingFragment;
import com.bigjpg.ui.widget.SwipeViewPager;
import com.bigjpg.util.AppPref;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.EasyPermissions;
import com.bigjpg.util.LocalBroadcastUtil;
import com.bigjpg.util.ResourcesUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView, RadioGroup.OnCheckedChangeListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.main_viewPager)
    SwipeViewPager mViewPager;
    @BindView(R.id.main_tab_group)
    RadioGroup mTabGroup;

    public static final int TAB_INDEX_HOME = 0;
    public static final int TAB_INDEX_HISTORY = 1;
    public static final int TAB_INDEX_SETTING = 2;

    private MainPresenter mPresenter;
    private FragmentMainPagerAdapter mViewPagerAdapter;
    private BaseFragment mHomeFragment;
    private BaseFragment mHistoryFragment;
    private BaseFragment mSettingFragment;
    private int mLastTabId = R.id.main_home;
    private long mExitTime;

    private static final int RC_APP_PERMISSION = 11;

    public static void launch(Activity activity, int position){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EnlargeKey.TAB_POSITION, position);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        mTabGroup.setOnCheckedChangeListener(this);
        initFragment();

        mTabGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getAppConfig();
            }
        }, 500);

        registerBroadcastReceiver();
    }

    private void checkPermission() {
        if (!EasyPermissions.hasPermissions(this, AppUtil.REQUIRED_PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_tip), RC_APP_PERMISSION, AppUtil.REQUIRED_PERMISSIONS);
        }
    }

    @Override
    protected ViewGroup onCreateRootView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.activity_main, null, false);
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return null;
    }

    private void initFragment() {
        mHomeFragment = findFragmentByTag(this, HomeFragment.class);
        mHistoryFragment = findFragmentByTag(this, HistoryFragment.class);
        mSettingFragment = findFragmentByTag(this, SettingFragment.class);
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance();
        }
        if (mHistoryFragment == null) {
            mHistoryFragment = HistoryFragment.newInstance();
        }
        if (mSettingFragment == null) {
            mSettingFragment = SettingFragment.newInstance();
        }

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mHomeFragment);
        fragments.add(mHistoryFragment);
        fragments.add(mSettingFragment);

        mViewPagerAdapter = new FragmentMainPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(false);
        mViewPager.setOffscreenPageLimit(4);
        mTabGroup.check(R.id.main_home);
        mLastTabId = R.id.main_home;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseFragment> T findFragmentByTag(FragmentActivity activity, Class<T> cls) {
        return (T) activity.getSupportFragmentManager().findFragmentByTag(cls.getName());
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new MainPresenter();
        return mPresenter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int tabPosition = getTabPosition(intent);
        setMainTabPosition(tabPosition);
    }

    private int getTabPosition(Intent intent){
        return intent.getIntExtra(EnlargeKey.TAB_POSITION, TAB_INDEX_HOME);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == mLastTabId) {
            return;
        }

        RadioButton rButton = group.findViewById(checkedId);
        if (rButton.isChecked()) {
            int curTabIndex;
            switch (checkedId) {
                case R.id.main_home:
                    curTabIndex = TAB_INDEX_HOME;
                    break;
                case R.id.main_history:
                    curTabIndex = TAB_INDEX_HISTORY;
                    break;
                case R.id.main_setting:
                    curTabIndex = TAB_INDEX_SETTING;
                    break;
                default:
                    return;
            }
            mLastTabId = checkedId;
            mViewPager.setCurrentItem(curTabIndex, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == RC_APP_PERMISSION) {
            EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.permission_tip), R.string.ok, R.string.cancel, null, perms);
        }
    }

    protected void onBroadcastReceive(android.content.Context context, Intent intent) {
        String action = intent.getAction();
        if (AppIntent.ACTION_LOGOUT.equals(action)) {
            if (mHistoryFragment != null) {
                mHistoryFragment.onActivityEvent(EventType.LOGOUT, null);
            }
            if(mHomeFragment != null){
                mHomeFragment.onActivityEvent(EventType.LOGOUT, null);
            }
        } else if (AppIntent.ACTION_GET_CONFIG.equals(action)) {
            if (mPresenter != null) {
                mPresenter.getAppConfig();
            }
        } else if (AppIntent.ACTION_TASK_UPDATE.equals(action)) {
            Serializable data = intent.getSerializableExtra(EnlargeKey.TASK_LIST);
            if (data != null) {
                try {
                    List<EnlargeConfig> list = (List<EnlargeConfig>) data;
                    if (mHomeFragment != null) {
                        mHomeFragment.onActivityEvent(EventType.TASK_UPDATE, list);
                    }

                    if (mHistoryFragment != null) {
                        mHistoryFragment.onActivityEvent(EventType.TASK_UPDATE, list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (AppIntent.ACTION_TASK_DELETE.equals(action)) {
            Serializable data = intent.getSerializableExtra(EnlargeKey.ENLARGE_LOG);
            if (data instanceof EnlargeLog) {
                if (mHomeFragment != null) {
                    mHomeFragment.onActivityEvent(EventType.TASK_DELETE, data);
                }
            }
        } else if (AppIntent.ACTION_LOGIN.equals(action)) {

        } else if (AppIntent.ACTION_PERMISSION.equals(action)) {
            checkPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastUtil.unregisterReceiver(this, mBroadcastReceiver);
    }

    protected IntentFilter createBroadcastIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppIntent.ACTION_LOGIN);
        intentFilter.addAction(AppIntent.ACTION_LOGOUT);
        intentFilter.addAction(AppIntent.ACTION_GET_CONFIG);
        intentFilter.addAction(AppIntent.ACTION_TASK_UPDATE);
        intentFilter.addAction(AppIntent.ACTION_PERMISSION);
        intentFilter.addAction(AppIntent.ACTION_TASK_DELETE);
        return intentFilter;
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = createBroadcastIntentFilter();
        LocalBroadcastUtil.registerReceiver(this, mBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceive(context, intent);
        }
    };

    @Override
    public void setMainTabPosition(int position) {
        if (mViewPager != null && mViewPager.getCurrentItem() != position) {
            if (position == TAB_INDEX_HOME) {
                mTabGroup.check(R.id.main_home);
            } else if (position == TAB_INDEX_HISTORY) {
                mTabGroup.check(R.id.main_history);
            } else if (position == TAB_INDEX_SETTING) {
                mTabGroup.check(R.id.main_setting);
            }
        }
    }

    @Override
    public void onGetAppConfigSuccess(AppConfigResponse response) {
        if (response.getVersion() > AppUtil.getCurVersion(this) && response.getVersion() > AppPref.getInstance().getVersionCodeNot2Update()) {
            UpdateAppAlertDialog dialog = new UpdateAppAlertDialog(this, response);
            dialog.show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000L) {
                showToast(R.string.press_again_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
