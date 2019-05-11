package com.github.koston.photoview.fresco.page;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
    holder.pdvPage.setPhotoUri(imageBinder.getModelAtPosition(position).getUri());
    holder.pdvPage.setOnScaleChangeListener(
        (scaleFactor, focusX, focusY) ->
            imageBinder.getModelAtPosition(position).setScaled(holder.pdvPage.getScale() > 1.0f));
  }

  @Override
  public int getItemCount() {
    return imageBinder.getItemCount();
  }
}
