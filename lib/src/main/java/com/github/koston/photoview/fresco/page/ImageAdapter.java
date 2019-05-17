package com.github.koston.photoview.fresco.page;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.koston.photoview.fresco.CircleProgressBarDrawable;
import com.github.koston.photoview.fresco.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

  private final ImageBinder imageBinder;

  public ImageAdapter(ImageBinder imageBinder) {
    this.imageBinder = imageBinder;
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ImageViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.page, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    holder.pdvPage.setEnabled(imageBinder.getScalingEnabled());
    holder.pdvPage.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
    holder.pdvPage.setPhotoUri(imageBinder.getModelAtPosition(position).getUri());
    holder.pdvPage.setScale(0.1f);
    holder.pdvPage.setOnScaleChangeListener(
        (scaleFactor, scale, focusX, focusY) -> {
          if (scaleFactor > 1.0f) {
            if (scale >= 1.0f) {
              imageBinder.getModelAtPosition(position).setScaled(true);
            } else {
              imageBinder.getModelAtPosition(position).setScaled(false);
            }
          } else {
            if (scale <= 1.0f) {
              imageBinder.getModelAtPosition(position).setScaled(true);
            } else {
              imageBinder.getModelAtPosition(position).setScaled(false);
            }
          }
        });
  }

  @Override
  public int getItemCount() {
    return imageBinder.getItemCount();
  }
}
