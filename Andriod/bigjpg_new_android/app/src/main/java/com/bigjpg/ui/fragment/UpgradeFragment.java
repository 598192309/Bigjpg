package com.bigjpg.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigjpg.R;
import com.bigjpg.model.entity.PayType;
import com.bigjpg.model.entity.UpgradeItem;
import com.bigjpg.model.response.PaypalResponse;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.presenter.UpgradePresenter;
import com.bigjpg.mvp.view.UpgradeView;
import com.bigjpg.ui.activity.MainActivity;
import com.bigjpg.ui.adapter.UpgradeAdapter;
import com.bigjpg.ui.base.LoadingFragment;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;
import com.bigjpg.ui.viewholder.UpgradeItemViewHolder;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.DialogUtil;
import com.bigjpg.util.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 升级
 *
 * @author Momo
 * @date 2019-04-19 16:37
 */
public class UpgradeFragment extends LoadingFragment implements UpgradeView, UpgradeItemViewHolder.OnPayClickListener {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    private UpgradePresenter mPresenter;
    private List<UpgradeItem> mData = new ArrayList<>();
    private UpgradeAdapter mAdapter;

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new UpgradePresenter();
        return mPresenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.request();
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }

    @Override
    public void onGetUpgradeConfigSuccess(List<UpgradeItem> list) {
        mData.clear();
        mData.addAll(list);
        if (mAdapter == null) {
            mAdapter = new UpgradeAdapter(getActivity(), mData, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetUpgradeConfigFailed() {
    }

    @Override
    public void onGetPaypalOrderSuccess(PaypalResponse response) {
        AppUtil.callOuterBrowser(getActivity(), response.getUrl());
    }

    @Override
    public void onGetPaypalOrderFailed(PaypalResponse response) {
        if (response != null && response.getStatus() != null) {
            showMessageDialog(response.getStatus());
        } else {
            showMessageDialog(R.string.error);
        }
    }

    @Override
    public void onWechatPayClick(String type) {
        if (UserManager.getInstance().isLogin()) {
            SimpleBackUtil.show(getActivity(), SimpleBackPage.PayFragment, PayFragment.createLaunchBundle(type, PayType.WECHAT));
        } else {
            showLoginDialog();
        }
    }

    @Override
    public void onPaypalPayClick(String type) {
        if (UserManager.getInstance().isLogin()) {
            mPresenter.getPaypalOrder(type);
        } else {
            showLoginDialog();
        }
    }

    @Override
    public void onAlipayPayClick(String type) {
        if (UserManager.getInstance().isLogin()) {
            SimpleBackUtil.show(getActivity(), SimpleBackPage.PayFragment, PayFragment.createLaunchBundle(type, PayType.ALIPAY));
        } else {
            showLoginDialog();
        }
    }

    private void showLoginDialog() {
        DialogUtil.showTwoButtonTipDialog(getActivity(), getString(R.string.plz_login), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.launch(getActivity(), MainActivity.TAB_INDEX_SETTING);
            }
        });
    }
}
