package com.bigjpg.ui.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.mvp.presenter.LoadingPresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.ILoadingView;
import com.bigjpg.util.ResourcesUtil;


/**
 * LoadingFragment
 *
 */
public abstract class LoadingFragment extends BaseFragment implements ILoadingView {

    private View mLayoutContent;
    private View mLayoutLoading;
    private View mLayoutReload;
    private View mLayoutEmpty;
    private ViewStub mViewStubReload;
    private ViewStub mViewStubEmpty;
    private TextView mTvEmpty;
    private LinearLayout mTitleContainer;
    private LinearLayout mContentContainer;
    private View mRootView;

    private LoadingPresenter mLoadingPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = onCreateRootView(inflater, container, savedInstanceState);
        mTitleContainer = findViewById(mRootView, R.id.header_title);
        mContentContainer = findViewById(mRootView, R.id.content_view);
        View titleView = onCreateTitleView(inflater, container, savedInstanceState);
        View contentView = onCreateContentView(inflater, container, savedInstanceState);
        addTitleView(titleView);
        addContentView(contentView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findDefaultViews();
        initLoadingPresenter();
    }

    private void initLoadingPresenter() {
        Presenter presenter = getPresenter();
        if (presenter instanceof LoadingPresenter) {
            mLoadingPresenter = (LoadingPresenter) presenter;
        } else {
            throw new IllegalArgumentException("LoadingFragment must use a LoadingPresenter");
        }
    }

    protected void addTitleView(View titleView) {
        if (titleView != null && mTitleContainer != null && mTitleContainer.getChildCount() == 0) {
            mTitleContainer.addView(titleView);
        }
    }

    protected void addContentView(View contentView) {
        if (contentView != null) {
            mContentContainer.addView(contentView);
        }
    }

    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_loading, null);
    }

    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    private void findDefaultViews() {
        View view = getView();
        if (view != null) {
            mLayoutContent = findViewById(view, R.id.content_view);
            mLayoutLoading = findViewById(view, R.id.loading_view);
            mViewStubEmpty = findViewById(view, R.id.empty_viewstub);
            mViewStubReload = findViewById(view, R.id.reload_viewstub);
            if(mViewStubEmpty != null){
                onCreateEmptyView(mViewStubEmpty);
            }
            if(mViewStubReload != null){
                onCreateReloadView(mViewStubReload);
            }
        }
    }

    protected void setViewVisible(View view, boolean visible) {
        if (view != null) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        }
    }

    public void setLoadingViewHeight(int height) {
        if (mLayoutLoading == null || mViewStubEmpty == null || mViewStubReload == null) {
            return;
        }

        ViewGroup.LayoutParams params = mLayoutLoading.getLayoutParams();
        params.height = height;
        mLayoutLoading.setLayoutParams(params);
        mViewStubEmpty.setLayoutParams(params);
        mViewStubReload.setLayoutParams(params);
    }

    @Override
    public void showContentView() {
        setViewVisible(mLayoutContent, true);
        setViewVisible(mLayoutLoading, false);
        setViewVisible(mLayoutEmpty, false);
        setViewVisible(mLayoutReload, false);
    }

    @Override
    public void showLoadingView() {
        setViewVisible(mLayoutContent, false);
        setViewVisible(mLayoutLoading, true);
        setViewVisible(mLayoutEmpty, false);
        setViewVisible(mLayoutReload, false);
    }

    @Override
    public void showReloadView() {
        setViewVisible(mLayoutContent, false);
        setViewVisible(mLayoutLoading, false);
        setViewVisible(mLayoutEmpty, false);

        if (mLayoutReload == null && mViewStubReload != null) {
            mLayoutReload = mViewStubReload.inflate();
            onShowReloadView(mLayoutReload);
        }

        if (mLayoutReload != null) {
            setViewVisible(mLayoutReload, true);
            View view = mLayoutReload.findViewById(R.id.reload_btn);
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReloadClick();
                    }
                });
            }
        }
    }

    @Override
    public void showEmptyView(String text) {
        setViewVisible(mLayoutContent, false);
        setViewVisible(mLayoutLoading, false);
        setViewVisible(mLayoutReload, false);

        if (mLayoutEmpty == null && mViewStubEmpty != null) {
            mLayoutEmpty = mViewStubEmpty.inflate();
            mTvEmpty = findViewById(mLayoutEmpty, R.id.empty_text);
            onShowEmptyView(mLayoutEmpty);
        }

        if (mLayoutEmpty != null) {
            setViewVisible(mLayoutEmpty, true);
            if (mTvEmpty != null) {
                mTvEmpty.setText(text);
            }
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (mLoadingPresenter != null) {
            mLoadingPresenter.start();
        }
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (mLoadingPresenter != null) {
            mLoadingPresenter.stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLoadingPresenter != null) {
            mLoadingPresenter.setLoaded(false);
            mLoadingPresenter.setLoading(false);
        }
    }

    protected void onReloadClick() {
        if (mLoadingPresenter != null) {
            mLoadingPresenter.start();
        }
    }

    protected View getRootView() {
        return mRootView;
    }

    @Override
    public String getEmptyText() {
        return getString(R.string.no_data);
    }

    protected void onCreateEmptyView(ViewStub emptyViewStub) {
    }

    protected void onCreateReloadView(ViewStub reloadViewStub) {
    }

    protected void onShowEmptyView(View emptyView) {
    }

    protected void onShowReloadView(View reloadView) {
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
