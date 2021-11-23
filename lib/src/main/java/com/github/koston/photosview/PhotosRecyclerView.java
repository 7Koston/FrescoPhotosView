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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosRecyclerView extends RecyclerView {

  private ScaleGestureDetector gestureDetector;

  private boolean isScaling, isMultitouch;

  public PhotosRecyclerView(Context context) {
    super(context);
    init(context);
  }

  public PhotosRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PhotosRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    gestureDetector.onTouchEvent(ev);
    isMultitouch = ev.getPointerCount() > 1;
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    if (isScaling || isMultitouch) {
      return false;
    }
    return super.onInterceptTouchEvent(e);
  }

  public boolean isScaling() {
    return isScaling;
  }

  public boolean isMultitouch() {
    return isMultitouch;
  }

  private void init(Context context) {
    gestureDetector = new ScaleGestureDetector(context, new GestureListener());
  }

  private class GestureListener implements ScaleGestureDetector.OnScaleGestureListener {

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      isScaling = true;
      return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
      return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
      isScaling = false;
    }
  }
}
