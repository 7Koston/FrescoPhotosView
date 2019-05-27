package com.github.koston.photosview.fresco.example;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MaterialButton mbMain = findViewById(R.id.mbMain);

    mbMain.setOnClickListener(v -> startActivity(new Intent(this, SimpleActivity.class)));
  }
}
