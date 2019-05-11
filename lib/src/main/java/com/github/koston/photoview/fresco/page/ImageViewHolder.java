package com.github.koston.photoview.fresco.page;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.github.koston.photoview.fresco.R;
import com.github.koston.photoview.fresco.view.PhotoDraweeView;

public class ImageViewHolder extends ViewHolder {

  final PhotoDraweeView pdvPage;

  ImageViewHolder(@NonNull View view) {
    super(view);
    pdvPage = view.findViewById(R.id.pdvPage);
  }
}
