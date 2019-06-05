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

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {

  public static final Creator<ImageModel> CREATOR =
      new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
          return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
          return new ImageModel[size];
        }
      };
  private Uri uri;

  public ImageModel(String url) {
    uri = Uri.parse(url);
  }

  public ImageModel(Uri uri) {
    this.uri = uri;
  }

  private ImageModel(Parcel in) {
    uri = in.readParcelable(Uri.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(uri, flags);
  }

  public Uri getUri() {
    return uri;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }
}
