package com.github.koston.photoview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.github.koston.photoview.fresco.OnDismissListener;
import com.github.koston.photoview.fresco.OnViewMoveListener;
import com.github.koston.photoview.fresco.PhotoViewRecyclerView;
import com.github.koston.photoview.fresco.R;
import com.github.koston.photoview.fresco.SwipeDirectionDetector;
import com.github.koston.photoview.fresco.SwipeToDismissListener;
import com.github.koston.photoview.fresco.page.ImageAdapter;
import com.github.koston.photoview.fresco.page.ImageBinder;
import com.github.koston.photoview.fresco.page.ImageModel;
import java.util.ArrayList;
import java.util.List;

public class PhotosView extends FrameLayout implements ImageBinder, OnViewMoveListener {

  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;

  private boolean flingScroll;
  private boolean swipeToDismiss;
  private boolean scaling;
  private boolean backgroundFade;

  private int orientation;

  private ImageAdapter adapter;
  private LinearLayoutManager layoutManager;

  private PhotoViewRecyclerView recyclerView;
  private FrameLayout dismissView;

  private SwipeDirectionDetector directionDetector;
  private ScaleGestureDetector scaleDetector;
  private GestureDetectorCompat gestureDetector;

  private OnDismissListener dismissListener;
  private OnViewMoveListener moveListener;

  private SwipeToDismissListener swipeDismissListener;
  private SwipeDirectionDetector.Direction direction;

  private boolean wasScaled;

  private PagerSnapHelper snapHelper;

  private ArrayList<ImageModel> models;

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
  public void setOnClickListener(@Nullable OnClickListener l) {
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

    if (direction == null) {
      if (scaleDetector.isInProgress() || event.getPointerCount() > 1) {
        wasScaled = true;
        return recyclerView.dispatchTouchEvent(event);
      }
    }

    int curPos = layoutManager.findFirstVisibleItemPosition();
    for (int i = 0; i < adapter.getItemCount(); i++) {
      if (i != curPos) {
        models.get(i).setScaled(false);
      }
    }
    if (curPos != -1 && !models.get(curPos).isScaled()) {
      directionDetector.onTouchEvent(event);
      if (direction != null) {
        switch (direction) {
          case UP:
          case DOWN:
            if (swipeToDismiss && !wasScaled) {
              return swipeDismissListener.onTouch(dismissView, event);
            } else {
              break;
            }
          case LEFT:
          case RIGHT:
            return recyclerView.dispatchTouchEvent(event);
        }
      }
      return true;
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

    scaleDetector.onTouchEvent(event);
    gestureDetector.onTouchEvent(event);
  }

  private void onActionDown(MotionEvent event) {
    direction = null;
    wasScaled = false;
    recyclerView.dispatchTouchEvent(event);
    swipeDismissListener.onTouch(dismissView, event);
  }

  private void onActionUp(MotionEvent event) {
    swipeDismissListener.onTouch(dismissView, event);
    if (!recyclerView.isScrolled()) {
      recyclerView.dispatchTouchEvent(event);
    }
  }

  private void onClick(MotionEvent event, boolean isOverlayWasClicked) {
    /*if (overlayView != null && !isOverlayWasClicked) {
      AnimationUtils.animateVisibility(overlayView);
      super.dispatchTouchEvent(event);
    }*/
  }

  private boolean dispatchOverlayTouch(MotionEvent event) {
    /*return overlayView != null
    && overlayView.getVisibility() == View.VISIBLE
    && overlayView.dispatchTouchEvent(event);*/
    return true;
  }

  private void init(Context context, AttributeSet attrs) {
    FrameLayout.inflate(context, R.layout.activity, this);
    orientation = HORIZONTAL;
    flingScroll = false;
    swipeToDismiss = false;
    scaling = false;
    backgroundFade = false;
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PhotosView);
      orientation = ta.getInteger(R.styleable.PhotosView_orientation, HORIZONTAL);
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

    scaleDetector =
        new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener());

    gestureDetector =
        new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
        });
  }

  public void setFlingScroll(boolean flingScroll) {
    this.flingScroll = flingScroll;
  }

  public void setSwipeToDismiss(boolean swipeToDismiss) {
    this.swipeToDismiss = swipeToDismiss;
  }

  public void setScaling(boolean scaling) {
    this.scaling = scaling;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  public void setBackgroundFade(boolean backgroundFade) {
    this.backgroundFade = backgroundFade;
  }

  public void setMoveListener(OnViewMoveListener moveListener) {
    this.moveListener = moveListener;
  }

  public void setOnDismissListener(
      OnDismissListener dismissListener) {
    this.dismissListener = dismissListener;
    swipeDismissListener = new SwipeToDismissListener(dismissView, dismissListener, this);
    dismissView.setOnTouchListener(swipeDismissListener);
  }

  public void setModels(List<Uri> uris) {
    int size = 0;
    if (uris != null) {
      size = uris.size();
      this.models = new ArrayList<>(size);
    }
    for (int i = 0; i < size; i++) {
      this.models.add(new ImageModel(uris.get(i)));
    }
  }

  public void setModels(Uri... uris) {
    int size = 0;
    if (uris != null) {
      size = uris.length;
      this.models = new ArrayList<>(size);
    }
    for (int i = 0; i < size; i++) {
      this.models.add(new ImageModel(uris[i]));
    }
  }

  private void initRecyclerView() {
    if (orientation == HORIZONTAL) {
      layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
    } else {
      layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
    }
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
    return models != null ? models.size() : 0;
  }

  @Override
  public ImageModel getModelAtPosition(int position) {
    return models.get(position);
  }

  @Override
  public boolean getScalingEnabled() {
    return scaling;
  }
}
