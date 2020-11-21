package com.bigjpg.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bigjpg.R;
import com.bigjpg.model.constant.AppIntent;
import com.bigjpg.model.entity.PayType;
import com.bigjpg.model.response.PayResponse;
import com.bigjpg.mvp.presenter.PayPresenter;
import com.bigjpg.mvp.presenter.Presenter;
import com.bigjpg.mvp.view.PayView;
import com.bigjpg.ui.base.LoadingFragment;
import com.bigjpg.util.AppUtil;
import com.bigjpg.util.FileUtil;
import com.bigjpg.util.FrescoLoader;
import com.bigjpg.util.LocalBroadcastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Momo
 * @date 2019-04-22 11:12
 */
public class PayFragment extends LoadingFragment implements PayView {

    @BindView(R.id.qr_code)
    SimpleDraweeView mImage;
    @BindView(R.id.start_pay)
    Button mBtnStartPay;

    private PayPresenter mPresenter;
    private String mType;
    private int mPayType;
    private static final String KEY_TYPE = "type";
    private static final String KEY_PAY_TYPE = "pay_type";

    private boolean mImageLoaded;
    private String mImageUrl;
    private String mPayUrl;

    /**
     *
     * @param type 商品类型
     * @param payType 支付类型
     * @return
     */
    public static Bundle createLaunchBundle(String type, int payType) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        bundle.putInt(KEY_PAY_TYPE, payType);
        return bundle;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mType = bundle.getString(KEY_TYPE);
        mPayType = bundle.getInt(KEY_PAY_TYPE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.request();
    }

    @Override
    protected Presenter onCreatePresenter() {
        mPresenter = new PayPresenter(mType, mPayType);
        return mPresenter;
    }

    @OnClick(R.id.qr_save)
    void onSaveClick() {
        if (mImageLoaded) {
            if(!AppUtil.hasRequiredPermission()){
                LocalBroadcastUtil.sendBroadcast(getContext(), AppIntent.ACTION_PERMISSION);
                return;
            }

            showLoadingDialog(R.string.begin_download);
            saveQRCodeToGallery();
        } else {
            showMessageDialog("Please wait");
        }
    }

    @OnClick(R.id.start_pay)
    void onStartPayClick(){
        if(mPayUrl != null){
            AppUtil.callOuterBrowser(getActivity(), mPayUrl);
        }
    }

    @Override
    public void getGetPayQrSuccess(PayResponse response) {
        String qr = response.getQr();
        if (!TextUtils.isEmpty(qr)) {
            mImageUrl = qr;
            FrescoLoader.loadImage(mImage, qr, new OnQrCodeLoadedListener(this));
        }

        if(mPayType == PayType.ALIPAY){
            String qrUrl = response.getQr_url();
            if(!TextUtils.isEmpty(qrUrl)){
                mPayUrl = qrUrl;
                mBtnStartPay.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onLoadQrCodeSuccess() {
        mImageLoaded = true;
    }

    private void onLoadQrCodeFailed() {
        mImageLoaded = false;
    }

    private void saveQRCodeToGallery() {
        FrescoLoader.getBitmap(getContext(), mImageUrl, new FrescoLoader.OnLoadBitmapListener() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap) {
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            String imagePath = File.separator +  "bigjpg_payment_qr_code_" + System.currentTimeMillis() + ".jpg";
                            String simplePath = AppUtil.getBijpgSimpleFilePath() + imagePath;
                            String filePath = AppUtil.getBigjpgFilePath() + imagePath;
                            boolean saved = FileUtil.saveBitmap(bitmap, filePath, Bitmap.CompressFormat.JPEG);
                            if (saved) {
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                                showToast(getString(R.string.save_to) + simplePath);
                            } else {
                                showToast(R.string.error);
                            }
                        }
                    });
                }
            }

            @Override
            public void onBitmapLoadFailure() {
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingDialog();
                            showToast(R.string.error);
                        }
                    });
                }
            }
        });
    }

    private static class OnQrCodeLoadedListener implements FrescoLoader.OnImageLoadedListener {

        private WeakReference<PayFragment> mReference;

        private OnQrCodeLoadedListener(PayFragment payFragment) {
            mReference = new WeakReference<>(payFragment);
        }

        @Override
        public void onImageLoadedSuccess(String id, ImageInfo imageInfo, Animatable animatable) {
            PayFragment payFragment = mReference.get();
            if (payFragment != null && payFragment.isAdded()) {
                payFragment.onLoadQrCodeSuccess();
            }
        }

        @Override
        public void onImageLoadedFailed() {
            PayFragment payFragment = mReference.get();
            if (payFragment != null && payFragment.isAdded()) {
                payFragment.onLoadQrCodeFailed();
            }
        }
    }
}
