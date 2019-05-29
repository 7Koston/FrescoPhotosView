/*
 * Copyright 2019 7Koston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.koston.photosview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.koston.photosview.page.ImageAdapter;
import com.github.koston.photosview.page.ImageBinder;
import com.github.koston.photosview.page.ImageModel;
import java.util.ArrayList;
import java.util.List;

public class PhotosView extends FrameLayout implements ImageBinder, OnViewMoveListener {

  private boolean flingScroll;
  private boolean swipeToDismiss;
  private boolean scaling;
  private boolean backgroundFade;

  private ImageAdapter adapter;
  private LinearLayoutManager layoutManager;

  private RecyclerViewViewPager recyclerView;
  private FrameLayout dismissView;

  private SwipeDirectionDetector directionDetector;
  private SwipeDirectionDetector.Direction direction;

  private OnDismissListener dismissListener;
  private OnViewMoveListener moveListener;
  private SwipeToDismissListener swipeDismissListener;

  private float currentPosScale;

  private PagerSnapHelper snapHelper;

  private ImageRequestBuilder imageRequest;

  private GenericDraweeHierarchyBuilder draweeHierarchy;

  private ArrayList<ImageModel> items;

  private @IdRes
  int pageViewId;

  private @LayoutRes
  int pageLayout;

  public PhotosView(Context context) {
    super(context);
    init(context, null);
  }

  public PhotosView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public PhotosView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @Override
  public void setOnClickListener(@Nullable View.OnClickListener l) {
    View view = this;
    /*    gestureDetector =
    new GestureDetectorCompat(
        this.getContext(),
        new GestureDetector.SimpleOnGestureListener() {
          @Override
          public boolean onSingleTapConfirmed(MotionEvent e) {
            if (l != null) {
              l.onClick(view);
              return true;
            }
            return super.onSingleTapConfirmed(e);
          }
        });*/
  }

  public void setOnClickListener(@Nullable OnClickListener l) {
    View view = this;
  }

  @Override
  public void onViewMove(float translationY, int translationLimit) {
    if (backgroundFade) {
      float alpha = 255 - Math.abs(translationY * 255 / (translationLimit * 4f));
      Drawable background = getBackground();
      if (background != null) {
        background.setAlpha((int) alpha);
      }
    }
    if (moveListener != null) {
      moveListener.onViewMove(translationY, translationLimit);
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    onUpDownEvent(event);

    if (!recyclerView.isScaling() && !recyclerView.isMultitouch()) {
      directionDetector.onTouchEvent(event);
      if (direction != null) {
        switch (direction) {
          case UP:
          case DOWN:
            if (swipeToDismiss && currentPosScale == 1f)
              return swipeDismissListener.onTouch(dismissView, event);
            else {
              break;
            }
/*          case LEFT:
          case RIGHT:
            if (currentPosScale == 1f) return recyclerView.dispatchTouchEvent(event);
            else break;*/
        }
      }
    }
    return super.dispatchTouchEvent(event);
  }

  private void onUpDownEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      onActionUp(event);
    }
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      onActionDown(event);
    }
  }

  private void onActionDown(MotionEvent event) {
    //recyclerView.dispatchTouchEvent(event);
    swipeDismissListener.onTouch(dismissView, event);
  }

  private void onActionUp(MotionEvent event) {
    swipeDismissListener.onTouch(dismissView, event);
  }

  private void init(Context context, AttributeSet attrs) {
    FrameLayout.inflate(context, R.layout.photos_view, this);
    flingScroll = false;
    swipeToDismiss = false;
    scaling = false;
    backgroundFade = false;
    currentPosScale = 1f;
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PhotosView);
      flingScroll = ta.getBoolean(R.styleable.PhotosView_enableFlingScroll, false);
      swipeToDismiss = ta.getBoolean(R.styleable.PhotosView_enableSwipeToDismiss, false);
      scaling = ta.getBoolean(R.styleable.PhotosView_enableScaling, false);
      backgroundFade = ta.getBoolean(R.styleable.PhotosView_enableBackgroundFade, false);

      ta.recycle();
    }
    recyclerView = findViewById(R.id.pvrvPhotosViewRecyclerView);
    dismissView = findViewById(R.id.flPhotosViewDismissView);

    initRecyclerView();

    swipeDismissListener = new SwipeToDismissListener(dismissView, dismissListener, this);
    dismissView.setOnTouchListener(swipeDismissListener);

    directionDetector =
        new SwipeDirectionDetector(context) {
          @Override
          public void onDirectionDetected(Direction dir) {
            direction = dir;
          }
        };
  }

  public void setFlingScroll(boolean flingScroll) {
    this.flingScroll = flingScroll;
    initRecyclerView();
  }

  public void setSwipeToDismiss(boolean swipeToDismiss) {
    this.swipeToDismiss = swipeToDismiss;
  }

  public void setScaling(boolean scaling) {
    this.scaling = scaling;
  }

  public void setBackgroundFade(boolean backgroundFade) {
    this.backgroundFade = backgroundFade;
  }

  public void setMoveListener(OnViewMoveListener moveListener) {
    this.moveListener = moveListener;
  }

  public void setPageLayout(@LayoutRes int pageLayout, @IdRes int pageViewId) {
    this.pageLayout = pageLayout;
    this.pageViewId = pageViewId;
  }

  public void setOnDismissListener(OnDismissListener dismissListener) {
    this.dismissListener = dismissListener;
    swipeDismissListener = new SwipeToDismissListener(dismissView, dismissListener, this);
    dismissView.setOnTouchListener(swipeDismissListener);
  }

  public void setItems(List<Uri> uris) {
    if (items != null) {
      items.clear();
    }
    int size = 0;
    if (uris != null) {
      size = uris.size();
      items = new ArrayList<>(size);
    }
    for (int i = 0; i < size; i++) {
      items.add(new ImageModel(uris.get(i)));
    }
  }

  public void setItems(Uri... uris) {
    if (items != null) {
      items.clear();
    }
    int size = 0;
    if (uris != null) {
      size = uris.length;
      items = new ArrayList<>(size);
    }
    for (int i = 0; i < size; i++) {
      items.add(new ImageModel(uris[i]));
    }
  }

  public void addItem(Uri uri) {
    if (items == null) {
      items = new ArrayList<>();
    }
    if (adapter != null) {
      items.add(new ImageModel(uri));
      adapter.notifyItemInserted(items.size() - 1);
    }
  }

  public void addAll(Uri... uris) {
    if (items == null) {
      items = new ArrayList<>();
    }
    for (Uri uri : uris) {
      items.add(new ImageModel(uri));
    }
    if (adapter != null) {
      adapter.notifyItemRangeChanged(items.size(), uris.length);
    }
  }

  public void addAll(List<Uri> uris) {
    if (items == null) {
      items = new ArrayList<>();
    }
    for (Uri uri : uris) {
      items.add(new ImageModel(uri));
    }
    if (adapter != null) {
      adapter.notifyItemRangeChanged(items.size(), uris.size());
    }
  }

  public void removeItem(Uri uri) {
    if (items != null && adapter != null) {
      for (int i = 0; i < items.size(); i++) {
        if (items.get(i).getUri() == uri) {
          if (items.remove(i) != null) {
            adapter.notifyItemRemoved(i);
          }
        }
      }
    }
  }

  public void removeItem(int position) {
    if (items != null && adapter != null) {
      if (items.remove(position) != null) {
        adapter.notifyItemRemoved(position);
      }
    }
  }

  private void initRecyclerView() {
    if (layoutManager == null)
      layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
    if (adapter == null) {
      adapter = new ImageAdapter(this);
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(layoutManager);
    }
    if (snapHelper == null) {
      snapHelper = new PagerSnapHelper();
    }
    if (!flingScroll) {
      snapHelper.attachToRecyclerView(recyclerView);
    } else {
      snapHelper.attachToRecyclerView(null);
    }
  }

  @Override
  public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  @Override
  public ImageModel getModelAtPosition(int position) {
    return items.get(position);
  }

  @Override
  public int getPageViewId() {
    if (pageViewId != 0) {
      return pageViewId;
    } else {
      return R.id.pdvPage;
    }
  }

  @Override
  public int getPageLayout() {
    if (pageLayout != 0) {
      return pageLayout;
    } else {
      return R.layout.photos_view_page;
    }
  }

  @Override
  public boolean getScalingEnabled() {
    return scaling;
  }

  @Override
  public void onScalingChange(int position, float scale) {
    currentPosScale = scale;
  }

  @Override
  public ImageRequestBuilder getImageRequestBuilder() {
    return imageRequest;
  }

  public void setImageRequestBuilder(ImageRequestBuilder imageRequest) {
    this.imageRequest = imageRequest;
  }

  @Override
  public GenericDraweeHierarchyBuilder getDraweeHierarchyBuilder() {
    return draweeHierarchy;
  }

  public void setDraweeHierarchyBuilder(GenericDraweeHierarchyBuilder draweeHierarchy) {
    this.draweeHierarchy = draweeHierarchy;
    this.draweeHierarchy.setActualImageScaleType(ScaleType.FIT_CENTER);
  }

  public interface OnClickListener {

    void onClick(@NonNull View view, float x, float y);
  }
}
