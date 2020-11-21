package com.bigjpg.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import androidx.annotation.DrawableRes;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * 描述:FrescoLoader
 *
 * @Author mfx
 * @Created at 2015年12月30日 11:47
 */
public class FrescoLoader {

    public static void loadImage(SimpleDraweeView draweeView, String url) {
        loadImage(draweeView, url, null);
    }

    public static void loadImage(SimpleDraweeView draweeView, String url, OnImageLoadedListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        if (listener == null) {
            draweeView.setImageURI(uri);
        }else {
            ImageLoadingControllerListener controllerListener = new ImageLoadingControllerListener();
            controllerListener.setOnImageLoadedListener(listener);
            PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
            builder.setControllerListener(controllerListener);
            builder.setOldController(draweeView.getController());
            builder.setUri(uri);
            DraweeController controller = builder.build();
            draweeView.setController(controller);
        }
    }

    public static void loadImageFromFile(SimpleDraweeView draweeView, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        loadImageFromFile(draweeView, filePath, false);
    }

    public static void loadImage(SimpleDraweeView draweeView, String url, int width, int height) {
        Uri uri = Uri.parse(url);
        ResizeOptions options = new ResizeOptions(width, height);
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(options)
                .build();
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        builder.setOldController(draweeView.getController());
        builder.setImageRequest(request);
        DraweeController controller = builder.build();
        draweeView.setController(controller);
    }

    public static void loadImageFromFile(SimpleDraweeView draweeView, String filePath, int width, int height) {
        Uri uri = Uri.parse("file://" + filePath);
        ResizeOptions options = new ResizeOptions(width, height);
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(options)
                .build();
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        builder.setOldController(draweeView.getController());
        builder.setImageRequest(request);
        DraweeController controller = builder.build();
        draweeView.setController(controller);
    }

    public static void clearFileCache(String url) {
        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromDiskCache(uri);
    }

    public static void clearMemoryFileCache(String filePath) {
        Uri uri = createLocalImageUri(filePath);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(uri);
    }

    public static void clearCache(Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromCache(uri);
    }

    public static void clearMemoryCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }

    /**
     * 从本地加载图片
     *
     * @param draweeView
     * @param filePath
     * @param isClearMemory
     */
    public static void loadImageFromFile(SimpleDraweeView draweeView, String filePath, boolean isClearMemory) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        Uri uri = createLocalImageUri(filePath);
        if (isClearMemory) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(uri);
        }
        draweeView.setImageURI(uri);
    }

    public static void loadImageFromDrawable(SimpleDraweeView draweeView, @DrawableRes int res) {
        Uri imageUri = createDrawableImageUri(res);
        draweeView.setImageURI(imageUri);
    }

    /**
     * 高斯模糊显示
     *
     * @param draweeView
     * @param url
     * @param iterations 迭代次数，越大越魔化
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊
     */
    public static void loadBlurImage(SimpleDraweeView draweeView, String url, int iterations, int blurRadius) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadProgressiveImage(SimpleDraweeView draweeView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
    }

    public static Uri createRemoteImageUri(String url) {
        return Uri.parse(url);
    }

    public static Uri createLocalImageUri(String filePath) {
        return Uri.parse("file://" + filePath);
    }

    public static Uri createDrawableImageUri(@DrawableRes int res) {
        Uri uri = new Uri.Builder()
                .scheme(com.facebook.common.util.UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(res))
                .build();
        return uri;
    }

    public static boolean isInBitmapMemoryCache(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline != null) {
            return imagePipeline.isInBitmapMemoryCache(createRemoteImageUri(url));
        } else {
            return false;
        }
    }

    public static boolean isInBitmapMemoryCache(Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline != null) {
            return imagePipeline.isInBitmapMemoryCache(uri);
        } else {
            return false;
        }
    }

    public static boolean isInBitmapMemoryCache(ImageRequest request) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline != null) {
            return imagePipeline.isInBitmapMemoryCache(request);
        } else {
            return false;
        }
    }


    public static boolean isInBitmapMemoryCache(Uri uri, int width, int height) {
        ResizeOptions options = new ResizeOptions(width, height);
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(options)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline != null) {
            return imagePipeline.isInBitmapMemoryCache(request);
        } else {
            return false;
        }
    }


    public static File getCacheFile(Uri uri) {
        File result = null;
        BinaryResource cache = Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(uri.toString()));
        if (cache instanceof FileBinaryResource) {
            FileBinaryResource fileBinaryResource = (FileBinaryResource) cache;
            result = fileBinaryResource.getFile();
        }
        return result;
    }

    public static void getBitmap(Context context, Uri uri, final OnLoadBitmapListener onLoadBitmapListener) {
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            if (onLoadBitmapListener != null) {
                                onLoadBitmapListener.onBitmapLoaded(bitmap);
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (onLoadBitmapListener != null) {
                    onLoadBitmapListener.onBitmapLoadFailure();
                }
            }
        };
        getBitmap(context, uri, 0, 0, dataSubscriber);
    }

    public static void getBitmap(Context context, String url, final OnLoadBitmapListener onLoadBitmapListener) {
        Uri uri = Uri.parse(url);
        getBitmap(context, uri, onLoadBitmapListener);
    }


    public interface OnLoadBitmapListener {
        void onBitmapLoaded(Bitmap bitmap);

        void onBitmapLoadFailure();
    }

    /**
     * getBitmap
     *
     * @param context
     * @param uri
     * @param width
     * @param height
     * @param dataSubscriber
     */
    public static void getBitmap(Context context, Uri uri, int width, int height, DataSubscriber dataSubscriber) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, context.getApplicationContext());
        dataSource.subscribe(dataSubscriber, CallerThreadExecutor.getInstance());
    }

    public static boolean isGif(String path) {
        if (path == null) {
            return false;
        } else {
            return path.toLowerCase().endsWith(".gif");
        }
    }

    public static boolean isGifFormat(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;
        if (TextUtils.isEmpty(type)) {
            return false;
        } else {
            return "image/gif".equals(type);
        }
    }

    public static class ImageLoadingControllerListener extends BaseControllerListener<ImageInfo> {

        private OnImageLoadedListener listener;

        public void setOnImageLoadedListener(OnImageLoadedListener listener) {
            this.listener = listener;
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            if (listener != null) {
                listener.onImageLoadedSuccess(id, imageInfo, animatable);
            }
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            if (listener != null) {
                listener.onImageLoadedFailed();
            }
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoadedSuccess(String id, ImageInfo imageInfo, Animatable animatable);

        void onImageLoadedFailed();
    }
}
