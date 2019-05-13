package com.github.koston.photoview.fresco;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoViewRecyclerView extends RecyclerView {

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
