package com.bigjpg.ui.viewholder;

import android.view.View;
import android.view.ViewGroup;

import com.bigjpg.R;
import com.bigjpg.ui.simpleback.SimpleBackPage;
import com.bigjpg.ui.simpleback.SimpleBackUtil;
import com.bigjpg.util.AppManager;
import com.bigjpg.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class HeaderTaskViewHolder extends BaseTaskViewHolder {

    private OnChooseImageClickListener listener;

    @BindView(R.id.over_limit_tips)
    View overLimitView;

    public HeaderTaskViewHolder(View view) {
        super(view);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @OnClick(R.id.choose_image)
    void onChooseImageClick() {
        if(listener != null){
            listener.onChooseImageClick();
        }
    }

    public void showOverLimitTipsView(){
        ViewUtil.setViewVisible(overLimitView, true);
    }

    public void hideOverLimitTipsView(){
        ViewUtil.setViewVisible(overLimitView, false);
    }

    @OnClick(R.id.over_limit_tips)
    void onOverLimitClick(){
        SimpleBackUtil.show(AppManager.getInstance().getCurrentActivity(), SimpleBackPage.UpgradeFragment);
    }

    public void setOnChooseImageClickListener(OnChooseImageClickListener listener){
        this.listener = listener;
    }

    public interface  OnChooseImageClickListener{
        void onChooseImageClick();
    }
}
