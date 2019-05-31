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

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

  private final ImageBinder imageBinder;

  public ImageAdapter(ImageBinder imageBinder) {
    this.imageBinder = imageBinder;
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ImageViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(imageBinder.getPageLayout(), parent, false),
        imageBinder.getPageViewId());
  }

  @Override
  public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    ImageRequestBuilder request = imageBinder.getImageRequestBuilder();
    if (request != null) {
      holder.pdvPage.setImageRequest(request.build());
    }
    GenericDraweeHierarchyBuilder hierarchy = imageBinder.getDraweeHierarchyBuilder();
    if (hierarchy != null) {
      holder.pdvPage.setHierarchy(hierarchy.build());
    }
    holder.pdvPage.setEnabled(imageBinder.getScalingEnabled());
    holder.pdvPage.setImageURI(imageBinder.getModelAtPosition(position).getUri());
  }

  @Override
  public int getItemCount() {
    return imageBinder.getItemCount();
  }

}
