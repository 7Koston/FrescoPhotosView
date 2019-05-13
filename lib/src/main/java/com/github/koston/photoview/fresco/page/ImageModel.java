package com.github.koston.photoview.fresco.page;

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
  private boolean scaled = false;

  public ImageModel(String url) {
    uri = Uri.parse(url);
  }

  public ImageModel(Uri uri) {
    this.uri = uri;
  }

  public ImageModel(Parcel in) {
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

  public boolean isScaled() {
    return scaled;
  }

  public void setScaled(boolean scaled) {
    this.scaled = scaled;
  }
}
