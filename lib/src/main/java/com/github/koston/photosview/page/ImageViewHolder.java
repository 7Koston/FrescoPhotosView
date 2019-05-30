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

import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.github.koston.photosview.view.PhotoDraweeView;

public class ImageViewHolder extends ViewHolder {

  public final PhotoDraweeView pdvPage;

  ImageViewHolder(@NonNull View view, @IdRes int id) {
    super(view);
    pdvPage = view.findViewById(id);
  }
}
