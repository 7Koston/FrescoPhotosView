package com.github.koston.photosview.fresco.example;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public final class OkHttpModule {

  private static OkHttpClient frescoHttpClient;

  public static void initialize() {
    if (frescoHttpClient == null) {
      buildFrescoHttpClient();
    }
  }

  private static void buildFrescoHttpClient() {
    OkHttpClient.Builder builder =
        new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(
                chain -> {
                  Request request = chain.request();
                  request = request.newBuilder().header("Accept", "image/webp").build();
                  return chain.proceed(request);
                })
            .hostnameVerifier((hostname, session) -> true);
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addNetworkInterceptor(logging);
    }
    frescoHttpClient = builder.build();
  }

  public static OkHttpClient getFrescoHttpClient() {
    return frescoHttpClient;
  }
}
