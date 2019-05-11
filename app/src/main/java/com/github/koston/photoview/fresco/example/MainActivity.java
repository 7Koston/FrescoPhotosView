package com.github.koston.photoview.fresco.example;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.koston.photoview.fresco.FrescoPhotoView;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MaterialButton mbMain = findViewById(R.id.mbMain);

    mbMain.setOnClickListener(
        v ->
            new FrescoPhotoView.Builder()
                .setModels(
                    Uri.parse(
                        "https://ruranobe.ru/images/thumb/9/97/mknr_v1_303c4fed-c47f-40f2-8e54-709ae1e1f3ab_cover1.jpg/720px-mknr_v1_303c4fed-c47f-40f2-8e54-709ae1e1f3ab_cover1.jpg"),
                    Uri.parse(
                        "https://ruranobe.ru/images/thumb/5/54/OreGairu_v01_a.jpg/720px-OreGairu_v01_a.jpg"),
                    Uri.parse(
                        "https://ruranobe.ru/images/thumb/3/35/TnYnN_v01_a.png/720px-TnYnN_v01_a.jpg"),
                    Uri.parse(
                        "https://ruranobe.ru/images/thumb/8/85/NGNL_v01_a.jpg/720px-NGNL_v01_a.jpg"))
                .setShowStatusBar(false)
                .show(this));
  }
}
