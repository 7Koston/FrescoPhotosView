/*
 * Copyright 2018 Freeman
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

package com.github.koston.photosview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class PhotoDraweeView extends SimpleDraweeView {

  private PhotoViewAttacher attacher;
  private ImageView.ScaleType pendingScaleType;

  public PhotoDraweeView(Context context) {
    this(context, null);
  }

  public PhotoDraweeView(Context context, AttributeSet attr) {
    this(context, attr, 0);
  }

  public PhotoDraweeView(Context context, AttributeSet attr, int defStyle) {
    super(context, attr, defStyle);
    init();
  }

  private void init() {
    attacher = new DraweeViewAttacher(this);
    getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
    // We always pose as a Matrix scale type, though we can change to another scale type
    // via the attacher
    super.setScaleType(ImageView.ScaleType.MATRIX);
    // apply the previously applied scale type
    if (pendingScaleType != null) {
      setScaleType(pendingScaleType);
      pendingScaleType = null;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.concat(attacher.getImageMatrix());
    super.onDraw(canvas);
  }

  /**
   * Get the current {@link PhotoViewAttacher} for this view. Be wary of holding on to references to
   * this attacher, as it has a reference to this view, which, if a reference is held in the wrong
   * place, can cause memory leaks.
   *
   * @return the attacher.
   */
  public PhotoViewAttacher getAttacher() {
    return attacher;
  }

  @Override
  public ImageView.ScaleType getScaleType() {
    return attacher.getScaleType();
  }

  @Override
  public void setScaleType(ImageView.ScaleType scaleType) {
    if (attacher == null) {
      pendingScaleType = scaleType;
    } else {
      attacher.setScaleType(scaleType);
    }
  }

  @Override
  public Matrix getImageMatrix() {
    return attacher.getImageMatrix();
  }

  @Override
  public void setOnLongClickListener(View.OnLongClickListener l) {
    attacher.setOnLongClickListener(l);
  }

  @Override
  public void setOnClickListener(View.OnClickListener l) {
    attacher.setOnClickListener(l);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    // setImageBitmap calls through to this method
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  public void setImageURI(Uri uri) {
    super.setImageURI(uri);
    if (attacher != null) {
      attacher.update();
    }
  }

  @Override
  protected boolean setFrame(int l, int t, int r, int b) {
    boolean changed = super.setFrame(l, t, r, b);
    if (changed) {
      attacher.update();
    }
    return changed;
  }

  public void setRotationTo(float rotationDegree) {
    attacher.setRotationTo(rotationDegree);
  }

  public void setRotationBy(float rotationDegree) {
    attacher.setRotationBy(rotationDegree);
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  public boolean isZoomEnabled() {
    return attacher.isZoomEnabled();
  }

  public boolean isZoomable() {
    return attacher.isZoomable();
  }

  public void setZoomable(boolean zoomable) {
    attacher.setZoomable(zoomable);
  }

  public RectF getDisplayRect() {
    return attacher.getDisplayRect();
  }

  public void getDisplayMatrix(Matrix matrix) {
    attacher.getDisplayMatrix(matrix);
  }

  public boolean setDisplayMatrix(Matrix finalRectangle) {
    return attacher.setDisplayMatrix(finalRectangle);
  }

  public void getSuppMatrix(Matrix matrix) {
    attacher.getSuppMatrix(matrix);
  }

  public boolean setSuppMatrix(Matrix matrix) {
    return attacher.setDisplayMatrix(matrix);
  }

  public float getMinimumScale() {
    return attacher.getMinimumScale();
  }

  public void setMinimumScale(float minimumScale) {
    attacher.setMinimumScale(minimumScale);
  }

  public float getMediumScale() {
    return attacher.getMediumScale();
  }

  public void setMediumScale(float mediumScale) {
    attacher.setMediumScale(mediumScale);
  }

  public float getMaximumScale() {
    return attacher.getMaximumScale();
  }

  public void setMaximumScale(float maximumScale) {
    attacher.setMaximumScale(maximumScale);
  }

  public float getScale() {
    return attacher.getScale();
  }

  public void setScale(float scale) {
    attacher.setScale(scale);
  }

  public void setAllowParentInterceptOnEdge(boolean allow) {
    attacher.setAllowParentInterceptOnEdge(allow);
  }

  public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
    attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
  }

  public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
    attacher.setOnMatrixChangeListener(listener);
  }

  public void setOnPhotoTapListener(OnPhotoTapListener listener) {
    attacher.setOnPhotoTapListener(listener);
  }

  public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
    attacher.setOnOutsidePhotoTapListener(listener);
  }

  public void setOnViewTapListener(OnViewTapListener listener) {
    attacher.setOnViewTapListener(listener);
  }

  public void setOnViewDragListener(OnViewDragListener listener) {
    attacher.setOnViewDragListener(listener);
  }

  public void setOnScaleStateListener(OnScaleStateListener listener) {
    attacher.setOnScaleStateListener(listener);
  }

  public void setScale(float scale, boolean animate) {
    attacher.setScale(scale, animate);
  }

  public void setScale(float scale, float focalX, float focalY, boolean animate) {
    attacher.setScale(scale, focalX, focalY, animate);
  }

  public void setZoomTransitionDuration(int milliseconds) {
    attacher.setZoomTransitionDuration(milliseconds);
  }

  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
    attacher.setOnDoubleTapListener(onDoubleTapListener);
  }

  public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
    attacher.setOnScaleChangeListener(onScaleChangedListener);
  }

  public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
    attacher.setOnSingleFlingListener(onSingleFlingListener);
  }
}
