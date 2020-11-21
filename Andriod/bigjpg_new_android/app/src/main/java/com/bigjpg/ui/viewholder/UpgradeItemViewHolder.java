package com.bigjpg.ui.viewholder;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigjpg.R;
import com.bigjpg.model.entity.UpgradeItem;
import com.bigjpg.model.entity.User;
import com.bigjpg.util.ResourcesUtil;
import com.bigjpg.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Momo
 * @date 2019-04-19 17:27
 */
public class UpgradeItemViewHolder extends BaseListViewHolder<UpgradeItem> {

    @BindView(R.id.upgrade_title)
    TextView tvTitle;
    @BindView(R.id.upgrade_content)
    TextView tvContent;
    @BindView(R.id.upgrade_pay_1)
    Button btnPay1;
    @BindView(R.id.upgrade_pay_2)
    Button btnPay2;
    @BindView(R.id.upgrade_pay_3)
    Button btnPay3;
    @BindView(R.id.upgrade_hot)
    View hot;
    private OnPayClickListener listener;
    private UpgradeItem data;
    private Context mContext;

    public UpgradeItemViewHolder(View view) {
        super(view);
        mContext = view.getContext();
    }

    @Override
    public void bindViewData(UpgradeItem data, int dataPosition) {
        this.data = data;
        tvTitle.setText(data.title);
        int highlightColor;
        if (User.Version.NONE.equals(data.version)) {
            ViewUtil.setViewVisible(btnPay1, false);
            ViewUtil.setViewVisible(btnPay2, false);
            ViewUtil.setViewVisible(btnPay3, false);
            highlightColor = ResourcesUtil.getColor(mContext, R.color.text_black);
            tvTitle.setBackgroundResource(R.drawable.shape_item_gray_top);
            tvTitle.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_black));
        } else if (User.Version.BASIC.equals(data.version)) {
            ViewUtil.setViewVisible(btnPay1, true);
            ViewUtil.setViewVisible(btnPay2, true);
            ViewUtil.setViewVisible(btnPay3, true);
            btnPay1.setBackgroundResource(R.drawable.btn_purple);
            btnPay2.setBackgroundResource(R.drawable.btn_purple);
            highlightColor = ResourcesUtil.getColor(mContext, R.color.btn_purple);
            tvTitle.setBackgroundResource(R.drawable.shape_item_purple_top);
            tvTitle.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
        } else if (User.Version.STANDARD.equals(data.version)) {
            ViewUtil.setViewVisible(btnPay1, true);
            ViewUtil.setViewVisible(btnPay2, true);
            ViewUtil.setViewVisible(btnPay3, true);
            btnPay1.setBackgroundResource(R.drawable.btn_green_2);
            btnPay2.setBackgroundResource(R.drawable.btn_green_2);
            highlightColor = ResourcesUtil.getColor(mContext, R.color.btn_green);
            tvTitle.setBackgroundResource(R.drawable.shape_item_green_top);
            tvTitle.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
        } else if (User.Version.PRO.equals(data.version)) {
            ViewUtil.setViewVisible(btnPay1, true);
            ViewUtil.setViewVisible(btnPay2, true);
            ViewUtil.setViewVisible(btnPay3, true);
            btnPay1.setBackgroundResource(R.drawable.btn_blue);
            btnPay2.setBackgroundResource(R.drawable.btn_blue);
            highlightColor = ResourcesUtil.getColor(mContext, R.color.btn_blue);
            tvTitle.setBackgroundResource(R.drawable.shape_item_blue_top);
            tvTitle.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_white));
        } else {
            ViewUtil.setViewVisible(btnPay1, false);
            ViewUtil.setViewVisible(btnPay2, false);
            ViewUtil.setViewVisible(btnPay3, false);
            highlightColor = ResourcesUtil.getColor(mContext, R.color.text_black);
            tvTitle.setBackgroundResource(R.drawable.shape_item_gray_top);
            tvTitle.setTextColor(ResourcesUtil.getColor(mContext, R.color.text_black));
        }

        if(User.Version.STANDARD.equals(data.version)){
            ViewUtil.setViewVisible(hot, true);
        }else{
            ViewUtil.setViewVisible(hot, false);
        }

        if (data.content != null && !data.content.isEmpty()) {
            SpannableStringBuilder content = new SpannableStringBuilder();
            for (int i = 0, size = data.content.size(); i < size; i++) {
                CharSequence text = Html.fromHtml(data.content.get(i));
                if (i == 0 || i == 1) {
                    SpannableString spanStr = new SpannableString(text);
                    spanStr.setSpan(new ForegroundColorSpan(highlightColor), 0, spanStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content.append(spanStr);
                } else {
                    content.append(text);
                }
                if (i != (size - 1)) {
                    content.append("\n");
                }
            }
            tvContent.setText(content);
        } else {
            tvContent.setText("");
        }

    }

    @OnClick(R.id.upgrade_pay_1)
    void onPay1Click() {
        if(listener != null){
            listener.onWechatPayClick(data.version);
        }
    }

    @OnClick(R.id.upgrade_pay_2)
    void onPay2Click() {
        if(listener != null){
            listener.onAlipayPayClick(data.version);
        }
    }

    @OnClick(R.id.upgrade_pay_3)
    void onPay3Click() {
        if(listener != null){
            listener.onPaypalPayClick(data.version);
        }
    }

    public void setOnPayClickListener(OnPayClickListener listener){
        this.listener = listener;
    }

    public interface OnPayClickListener{
        void onWechatPayClick(String type);
        void onPaypalPayClick(String type);
        void onAlipayPayClick(String type);
    }
}
