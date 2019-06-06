/*
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

import android.view.MotionEvent;
import android.view.View;

public interface OnViewTapListener {

  /**
   * A callback to receive where the user taps on a ImageView. You will receive a callback if the
   * user taps anywhere on the view, tapping on 'whitespace' will not be ignored.
   *
   * @param view - View the user tapped.
   */
  void onViewTap(View view, MotionEvent event);
}
