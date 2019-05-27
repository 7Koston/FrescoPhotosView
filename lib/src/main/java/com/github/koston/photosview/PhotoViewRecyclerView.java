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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

class PhotoViewRecyclerView extends RecyclerView {

  private boolean isDisallowIntercept, isScrolled = false;

  public PhotoViewRecyclerView(@NonNull Context context) {
    super(context);
    setScrollStateListener();
  }

  public PhotoViewRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    setScrollStateListener();
  }

  public PhotoViewRecyclerView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setScrollStateListener();
  }

  @Override
  public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    isDisallowIntercept = disallowIntercept;
    super.requestDisallowInterceptTouchEvent(disallowIntercept);
  }

  @Override
  public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
    if (ev.getPointerCount() > 1 && isDisallowIntercept) {
      requestDisallowInterceptTouchEvent(false);
      boolean handled = super.dispatchTouchEvent(ev);
      requestDisallowInterceptTouchEvent(true);
      return handled;
    } else {
      return super.dispatchTouchEvent(ev);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (ev.getPointerCount() > 1) {
      return false;
    } else {
      try {
        return super.onInterceptTouchEvent(ev);
      } catch (IllegalArgumentException ex) {
        return false;
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    try {
      return super.onTouchEvent(ev);
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  public boolean isScrolled() {
    return isScrolled;
  }

  private void setScrollStateListener() {
    addOnScrollListener(
        new OnScrollListener() {
          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            isScrolled = newState != SCROLL_STATE_IDLE;
          }
        });
  }
}
