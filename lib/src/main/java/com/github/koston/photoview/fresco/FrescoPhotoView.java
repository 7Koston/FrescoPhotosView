package com.github.koston.photoview.fresco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.github.koston.photoview.fresco.page.ImageAdapter;
import com.github.koston.photoview.fresco.page.ImageBinder;
import com.github.koston.photoview.fresco.page.ImageModel;
import com.github.koston.photoview.fresco.utils.SystemUIUtils;
import java.util.List;

public class FrescoPhotoView extends AppCompatActivity
    implements ImageBinder, OnDismissListener, OnViewMoveListener {

  private boolean horizontal;
  private boolean showNavigationBar;
  private boolean showStatusBar;

  private boolean lightStatusBar;
  private boolean lightNavigationBar;

  private int backgroundColor;
  private int statusBarColor;
  private int navigationBarColor;

  private boolean scaling;
  private boolean swipeToDismiss;

  private ImageModel[] models;

  private PhotoViewRecyclerView rvActivity;

  private LinearLayoutManager layoutManager;

  private View backgroundView;
  private ViewGroup dismissContainer;

  private SwipeDirectionDetector directionDetector;
  private ScaleGestureDetector scaleDetector;
  private GestureDetectorCompat gestureDetector;

  private SwipeToDismissListener swipeDismissListener;

  private SwipeDirectionDetector.Direction direction;

  private boolean wasScaled;
  private boolean isOverlayWasClicked;

  public static void start(
      Context context,
      boolean horizontal,
      boolean showNavigationBar,
      boolean showStatusBar,
      boolean lightStatusBar,
      boolean lightNavigationBar,
      boolean scaling,
      boolean swipeToDismiss,
      int backgroundColor,
      int statusBarColor,
      int navigationBarColor,
      ImageModel[] models) {
    Intent starter = new Intent(context, FrescoPhotoView.class);
    starter.putExtra("horizontal", horizontal);
    starter.putExtra("showNavigationBar", showNavigationBar);
    starter.putExtra("showStatusBar", showStatusBar);
    starter.putExtra("lightStatusBar", lightStatusBar);
    starter.putExtra("lightNavigationBar", lightNavigationBar);
    starter.putExtra("scaling", scaling);
    starter.putExtra("swipeToDismiss", swipeToDismiss);
    starter.putExtra("backgroundColor", backgroundColor);
    starter.putExtra("statusBarColor", statusBarColor);
    starter.putExtra("navigationBarColor", navigationBarColor);
    starter.putExtra("models", models);
    context.startActivity(starter);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity);

    if (savedInstanceState == null) {
      extractArguments(getIntent().getExtras());
    } else {
      extractArguments(savedInstanceState);
    }

    getWindow()
        .getDecorView()
        .setSystemUiVisibility(
            SystemUIUtils.computeSystemUI(
                lightStatusBar, lightNavigationBar, !showStatusBar, !showNavigationBar));

    rvActivity = findViewById(R.id.pvrvPhotosViewRecyclerView);

    backgroundView.setBackgroundColor(backgroundColor);
    dismissContainer = findViewById(R.id.flPhotosViewDismissView);
    swipeDismissListener = new SwipeToDismissListener(dismissContainer, this, this);
    dismissContainer.setOnTouchListener(swipeDismissListener);

    directionDetector =
        new SwipeDirectionDetector(this) {
          @Override
          public void onDirectionDetected(Direction dir) {
            direction = dir;
          }
        };

    scaleDetector =
        new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener());

    gestureDetector =
        new GestureDetectorCompat(
            this,
            new GestureDetector.SimpleOnGestureListener() {
              @Override
              public boolean onSingleTapConfirmed(MotionEvent e) {
                onClick(e, isOverlayWasClicked);
                return false;
              }
            });

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      getWindow().setNavigationBarColor(navigationBarColor);
      getWindow().setStatusBarColor(statusBarColor);
    }

    initRecyclerView(rvActivity);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    backgroundView = null;
    models = null;
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("horizontal", horizontal);
    outState.putBoolean("showNavigationBar", showNavigationBar);
    outState.putBoolean("showStatusBar", showStatusBar);
    outState.putBoolean("lightStatusBar", lightStatusBar);
    outState.putBoolean("lightNavigationBar", lightNavigationBar);
    outState.putBoolean("scaling", scaling);
    outState.putBoolean("swipeToDismiss", swipeToDismiss);
    outState.putInt("backgroundColor", backgroundColor);
    outState.putInt("statusBarColor", statusBarColor);
    outState.putInt("navigationBarColor", navigationBarColor);
    outState.putParcelableArray("models", models);
  }

  private void extractArguments(Bundle args) {
    if (args != null) {
      horizontal = args.getBoolean("horizontal");
      showNavigationBar = args.getBoolean("showNavigationBar");
      showStatusBar = args.getBoolean("showStatusBar");
      lightStatusBar = args.getBoolean("lightStatusBar");
      lightNavigationBar = args.getBoolean("lightNavigationBar");
      scaling = args.getBoolean("scaling");
      swipeToDismiss = args.getBoolean("swipeToDismiss");
      backgroundColor = args.getInt("backgroundColor");
      statusBarColor = args.getInt("statusBarColor");
      navigationBarColor = args.getInt("navigationBarColor");
      Parcelable[] parcels = args.getParcelableArray("models");
      if (parcels != null) {
        models = new ImageModel[parcels.length];
        for (int i = 0; i < parcels.length; i++) {
          models[i] = (ImageModel) parcels[i];
        }
      }
    }
  }

  private void initRecyclerView(RecyclerView recyclerView) {
    ImageAdapter imageAdapter = new ImageAdapter(this);
    if (horizontal) {
      layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
    } else {
      layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(imageAdapter);
    new PagerSnapHelper().attachToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    return models != null ? models.length : 0;
  }

  @Override
  public ImageModel getModelAtPosition(int position) {
    return models[position];
  }

  @Override
  public boolean getScalingEnabled() {
    return scaling;
  }

  @Override
  public void onDismiss() {
    finish();
  }

  @Override
  public void onViewMove(float translationY, int translationLimit) {
    float alpha = 1.0f - (1.0f / translationLimit / 4) * Math.abs(translationY);
    backgroundView.setAlpha(alpha);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    onUpDownEvent(event);

    if (direction == null) {
      if (scaleDetector.isInProgress() || event.getPointerCount() > 1) {
        wasScaled = true;
        return rvActivity.dispatchTouchEvent(event);
      }
    }

    int curPos = layoutManager.findFirstVisibleItemPosition();
    for (int i = 0; i < getItemCount(); i++) {
      if (i != curPos) {
        models[i].setScaled(false);
      }
    }
    if (curPos != -1 && !models[curPos].isScaled()) {
      directionDetector.onTouchEvent(event);
      if (direction != null) {
        switch (direction) {
          case UP:
          case DOWN:
            if (swipeToDismiss && !wasScaled) {
              return swipeDismissListener.onTouch(dismissContainer, event);
            } else {
              break;
            }
          case LEFT:
          case RIGHT:
            return rvActivity.dispatchTouchEvent(event);
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
    rvActivity.dispatchTouchEvent(event);
    swipeDismissListener.onTouch(dismissContainer, event);
    isOverlayWasClicked = dispatchOverlayTouch(event);
  }

  private void onActionUp(MotionEvent event) {
    swipeDismissListener.onTouch(dismissContainer, event);
    if (!rvActivity.isScrolled()) {
      rvActivity.dispatchTouchEvent(event);
    }
    isOverlayWasClicked = dispatchOverlayTouch(event);
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

  public static class Builder {

    private boolean horizontal = true;
    private boolean showNavigationBar = false;
    private boolean showStatusBar = false;

    private boolean scaling = true;
    private boolean swipeToDismiss = true;

    private boolean lightStatusBar = false;
    private boolean lightNavigationBar = false;

    private int backgroundColor = Color.BLACK;
    private int statusBarColor = Color.BLACK;
    private int navigationBarColor = Color.BLACK;

    private ImageModel[] models;

    public Builder() {
    }

    public Builder setHorizontal(boolean horizontal) {
      this.horizontal = horizontal;
      return this;
    }

    public Builder setShowNavigationBar(boolean showNavigationBar) {
      this.showNavigationBar = showNavigationBar;
      return this;
    }

    public Builder setShowStatusBar(boolean showStatusBar) {
      this.showStatusBar = showStatusBar;
      return this;
    }

    public Builder setLightStatusBar(boolean lightStatusBar) {
      this.lightStatusBar = lightStatusBar;
      return this;
    }

    public Builder setLightNavigationBar(boolean lightNavigationBar) {
      this.lightNavigationBar = lightNavigationBar;
      return this;
    }

    public Builder setScaling(boolean scaling) {
      this.scaling = scaling;
      return this;
    }

    public Builder setSwipeToDismiss(boolean swipeToDismiss) {
      this.swipeToDismiss = swipeToDismiss;
      return this;
    }

    public Builder setBackgroundColor(int backgroundColor) {
      this.backgroundColor = backgroundColor;
      return this;
    }

    public Builder setStatusBarColor(int statusBarColor) {
      this.statusBarColor = statusBarColor;
      return this;
    }

    public Builder setNavigationBarColor(int navigationBarColor) {
      this.navigationBarColor = navigationBarColor;
      return this;
    }

    public Builder setModels(ImageModel[] models) {
      this.models = models;
      return this;
    }

    public Builder setModels(List<Uri> uris) {
      int size = 0;
      if (uris != null) {
        size = uris.size();
        this.models = new ImageModel[size];
      }
      for (int i = 0; i < size; i++) {
        this.models[i] = new ImageModel(uris.get(i));
      }
      return this;
    }

    public Builder setModels(Uri... uris) {
      int size = 0;
      if (uris != null) {
        size = uris.length;
        this.models = new ImageModel[size];
      }
      for (int i = 0; i < size; i++) {
        this.models[i] = new ImageModel(uris[i]);
      }
      return this;
    }

    public void show(Context context) {
      FrescoPhotoView.start(
          context,
          horizontal,
          showNavigationBar,
          showStatusBar,
          lightStatusBar,
          lightNavigationBar,
          scaling,
          swipeToDismiss,
          backgroundColor,
          statusBarColor,
          navigationBarColor,
          models);
    }
  }
}
