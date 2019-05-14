package com.github.koston.photoview.fresco.page;

public interface ImageBinder {

  int getItemCount();

  ImageModel getModelAtPosition(int position);

  boolean getScalingEnabled();
}
