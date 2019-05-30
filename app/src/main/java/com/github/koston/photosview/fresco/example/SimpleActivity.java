package com.github.koston.photosview.fresco.example;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.koston.photosview.PhotosView;

public class SimpleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_simple);

    PhotosView pvSimple = findViewById(R.id.pvSimple);

    pvSimple.setItems(
        Uri.parse(
            "https://novel.tl/images/thumb/9/97/mknr_v1_303c4fed-c47f-40f2-8e54-709ae1e1f3ab_cover1.jpg/720px-mknr_v1_303c4fed-c47f-40f2-8e54-709ae1e1f3ab_cover1.jpg"),
        Uri.parse(
            "https://novel.tl/images/thumb/5/54/OreGairu_v01_a.jpg/720px-OreGairu_v01_a.jpg"),
        Uri.parse("https://novel.tl/images/thumb/3/35/TnYnN_v01_a.png/720px-TnYnN_v01_a.jpg"),
        Uri.parse("https://novel.tl/images/thumb/8/85/NGNL_v01_a.jpg/720px-NGNL_v01_a.jpg"));

    pvSimple.setFlingScroll(false);
    pvSimple.setBackgroundFade(true);
    pvSimple.setScaling(true);
    pvSimple.setSwipeToDismiss(true);
    pvSimple.setBackgroundColor(Color.BLACK);
    pvSimple.setDraweeHierarchyBuilder(
        GenericDraweeHierarchyBuilder.newInstance(getResources())
            .setProgressBarImage(new CircleProgressBarDrawable()));
    pvSimple.setOnDismissListener(this::finish);
    pvSimple.setOnClickListener(
        (v, x, y) ->
            Toast.makeText(
                this.getApplicationContext(), "CLICK" + "\n" + x + "\n" + y, Toast.LENGTH_SHORT)
                .show());
  }
}
