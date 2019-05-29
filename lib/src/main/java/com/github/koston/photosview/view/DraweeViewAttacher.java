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

package com.github.koston.photosview.view;

import android.graphics.Matrix;
import android.graphics.RectF;
import com.facebook.drawee.view.GenericDraweeView;

/*******************************************************************************
 * Description: Extend PhotoViewAttacher to support Fresco.
 *
 * Author: Freeman
 *
 * Date: 2018/9/7
 *******************************************************************************/
public class DraweeViewAttacher extends PhotoViewAttacher {

  private GenericDraweeView imageView;

  public DraweeViewAttacher(GenericDraweeView image) {
    super(image);
    this.imageView = image;
  }

  @Override
  protected RectF getDisplayRect(Matrix matrix) {
    RectF displayRect = new RectF();
    imageView.getHierarchy().getActualImageBounds(displayRect);
    matrix.mapRect(displayRect);
    return displayRect;
  }
}
