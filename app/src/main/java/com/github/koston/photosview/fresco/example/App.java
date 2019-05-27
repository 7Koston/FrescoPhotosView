package com.github.koston.photosview.fresco.example;

import androidx.multidex.MultiDexApplication;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import java.util.HashSet;
import java.util.Set;

public class App extends MultiDexApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    //
    OkHttpModule.initialize();
    //
    if (BuildConfig.DEBUG) {
      Set<RequestListener> requestListeners = new HashSet<>();
      requestListeners.add(new RequestLoggingListener());
      Fresco.initialize(
          this.getApplicationContext(),
          OkHttpImagePipelineConfigFactory.newBuilder(
              getApplicationContext(), OkHttpModule.getFrescoHttpClient())
              .setRequestListeners(requestListeners)
              .build());
      FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    } else {
      Fresco.initialize(
          this.getApplicationContext(),
          OkHttpImagePipelineConfigFactory.newBuilder(
              getApplicationContext(), OkHttpModule.getFrescoHttpClient())
              .build());
    }
  }
}
