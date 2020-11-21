package com.bigjpg.ui.simpleback;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.ui.base.BaseActivity;
import com.bigjpg.ui.base.BaseFragment;
import com.bigjpg.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 包含Fragment作为主体的Activity
 *
 * @author Momo
 * @date 2019-01-30 11:23
 */
public class SimpleBackActivity extends BaseActivity {

    public static final String KEY_PAGE = "key_page";
    public static final String KEY_ARGS = "key_args";

    private static final String FRAGMENT_TAG_PREFIX = "simple_tag_";
    private WeakReference<Fragment> mFragment;

    @Override
    protected ViewGroup onCreateRootView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.activity_simple_back, null, false);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            LogUtil.e("SimpleBackActivity intent null");
            finish();
            return;
        }

        int pageValue = intent.getIntExtra(KEY_PAGE, -1);
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            LogUtil.e("SimpleBackActivity can not find page, value : " + pageValue);
            finish();
            return;
        }

        int titleId = page.getTitle();
        if (titleId > 0) {
            setTitle(titleId);
        } else {
            setTitle("");
        }

        createPageFragment(intent, page);
    }

    private void createPageFragment(Intent intent, SimpleBackPage page) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = null;
            String tag = getPageFragmentTag(page);
            if (isRecreated()) {
                fragment = fragmentManager.findFragmentByTag(tag);
            }
            if (fragment == null) {
                fragment = (Fragment) page.getClazz().newInstance();
                Bundle bundle = intent.getBundleExtra(KEY_ARGS);
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
                fragmentTransaction.commit();
            }
            mFragment = new WeakReference<>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("SimpleBackActivity cannot create fragment, value : " + page.getValue());
        }
    }

    private String getPageFragmentTag(SimpleBackPage page) {
        return FRAGMENT_TAG_PREFIX + page.getValue();
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragment.get();
            if (fragment != null && fragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragment != null && mFragment.get() instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragment.get();
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
