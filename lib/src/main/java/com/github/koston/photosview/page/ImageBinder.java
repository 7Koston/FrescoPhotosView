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

package com.github.koston.photosview.page;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.koston.photosview.view.OnViewTapListener;

public interface ImageBinder {

  @IdRes
  int getPageViewId();

  @LayoutRes
  int getPageLayout();

  int getItemCount();

  ImageModel getModelAtPosition(int position);

  boolean getScalingEnabled();

  ImageRequestBuilder getImageRequestBuilder();

  GenericDraweeHierarchyBuilder getDraweeHierarchyBuilder();

  OnViewTapListener getOnViewTapListener();
}
