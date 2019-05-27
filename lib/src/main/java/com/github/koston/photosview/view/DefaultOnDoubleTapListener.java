/*
 * Copyright 2018 ongakuer
 * Copyright 2018 Chris Banes
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

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

  protected Attacher mAttacher;

  public DefaultOnDoubleTapListener(Attacher attacher) {
    setPhotoDraweeViewAttacher(attacher);
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent e) {

    if (mAttacher == null) {
      return false;
    }
    DraweeView<GenericDraweeHierarchy> draweeView = mAttacher.getDraweeView();
    if (draweeView == null) {
      return false;
    }

    if (mAttacher.getOnPhotoTapListener() != null) {
      final RectF displayRect = mAttacher.getDisplayRect();

      if (null != displayRect) {
        final float x = e.getX(), y = e.getY();
        if (displayRect.contains(x, y)) {
          float xResult = (x - displayRect.left) / displayRect.width();
          float yResult = (y - displayRect.top) / displayRect.height();
          mAttacher.getOnPhotoTapListener().onPhotoTap(draweeView, xResult, yResult);
          return true;
        }
      }
    }

    if (mAttacher.getOnViewTapListener() != null) {
      mAttacher.getOnViewTapListener().onViewTap(draweeView, e.getX(), e.getY());
      return true;
    }

    return false;
  }

  @Override
  public boolean onDoubleTap(MotionEvent event) {
    if (mAttacher == null) {
      return false;
    }

    try {
      float scale = mAttacher.getScale();
      float x = event.getX();
      float y = event.getY();

      // min, mid, max
      if (scale < mAttacher.getMediumScale()) {
        mAttacher.setScale(mAttacher.getMediumScale(), x, y, true);
      } else if (scale >= mAttacher.getMediumScale() && scale < mAttacher.getMaximumScale()) {
        mAttacher.setScale(mAttacher.getMaximumScale(), x, y, true);
      } else {
        mAttacher.setScale(mAttacher.getMinimumScale(), x, y, true);
      }
    } catch (Exception e) {
      // Can sometimes happen when getX() and getY() is called
    }
    return true;
  }

  @Override
  public boolean onDoubleTapEvent(MotionEvent event) {
    return false;
  }

  public void setPhotoDraweeViewAttacher(Attacher attacher) {
    mAttacher = attacher;
  }
}
