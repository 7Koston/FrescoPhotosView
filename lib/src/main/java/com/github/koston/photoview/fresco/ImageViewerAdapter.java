package com.github.koston.photoview.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.util.HashSet;
import tl.novel.widgets.fullcover.adapter.RecyclingPagerAdapter;
import tl.novel.widgets.fullcover.adapter.RecyclingPagerViewHolder;
import tl.novel.widgets.fullcover.view.OnScaleChangeListener;
import tl.novel.widgets.fullcover.view.ZoomableDraweeView;

/*
 * Created by troy379 on 07.12.16.
 */
class ImageViewerAdapter
    extends RecyclingPagerAdapter<ImageViewerAdapter.ImageRecyclingPagerViewHolder> {

    private final Context context;
    private final ImageViewer.DataSet<?> dataSet;
    private final HashSet<ImageRecyclingPagerViewHolder> holders;
    private final ImageRequestBuilder imageRequestBuilder;
    private final GenericDraweeHierarchyBuilder hierarchyBuilder;
    private final boolean isZoomingAllowed;

    ImageViewerAdapter(
        Context context,
        ImageViewer.DataSet<?> dataSet,
        ImageRequestBuilder imageRequestBuilder,
        GenericDraweeHierarchyBuilder hierarchyBuilder,
        boolean isZoomingAllowed) {
        this.context = context;
        this.dataSet = dataSet;
        this.holders = new HashSet<>();
        this.imageRequestBuilder = imageRequestBuilder;
        this.hierarchyBuilder = hierarchyBuilder;
        this.isZoomingAllowed = isZoomingAllowed;
    }

    @Override
    public ImageRecyclingPagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZoomableDraweeView drawee = new ZoomableDraweeView(context);
        drawee.setEnabled(isZoomingAllowed);

        ImageRecyclingPagerViewHolder holder = new ImageRecyclingPagerViewHolder(drawee);
        holders.add(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(ImageRecyclingPagerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dataSet.getData().size();
    }

    boolean isScaled(int index) {
        for (ImageRecyclingPagerViewHolder holder : holders) {
            if (holder.position == index) {
                return holder.isScaled;
            }
        }
        return false;
    }

    void resetScale(int index) {
        for (ImageRecyclingPagerViewHolder holder : holders) {
            if (holder.position == index) {
                holder.resetScale();
                break;
            }
        }
    }

    String getUrl(int index) {
        return dataSet.format(index);
    }

    private BaseControllerListener<ImageInfo> getDraweeControllerListener(
        final ZoomableDraweeView drawee) {
        return new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                drawee.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        };
    }

    class ImageRecyclingPagerViewHolder extends RecyclingPagerViewHolder
        implements OnScaleChangeListener {

        private final ZoomableDraweeView drawee;
        private int position = -1;
        private boolean isScaled;

        ImageRecyclingPagerViewHolder(View itemView) {
            super(itemView);
            drawee = (ZoomableDraweeView) itemView;
        }

        void bind(int position) {
            this.position = position;

            tryToSetHierarchy();
            setController(dataSet.format(position));

            drawee.setOnScaleChangeListener(this);
        }

        @Override
        public void onScaleChange(float scaleFactor, float focusX, float focusY) {
            isScaled = drawee.getScale() > 1.0f;
        }

        void resetScale() {
            drawee.setScale(1.0f, true);
        }

        private void tryToSetHierarchy() {
            if (hierarchyBuilder != null) {
                hierarchyBuilder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                drawee.setHierarchy(hierarchyBuilder.build());
            }
        }

        private void setController(String url) {
            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
            controllerBuilder.setUri(url);
            controllerBuilder.setOldController(drawee.getController());
            controllerBuilder.setControllerListener(getDraweeControllerListener(drawee));
            if (imageRequestBuilder != null) {
                imageRequestBuilder.setSource(Uri.parse(url));
                controllerBuilder.setImageRequest(imageRequestBuilder.build());
            }
            drawee.setController(controllerBuilder.build());
        }
    }
}
