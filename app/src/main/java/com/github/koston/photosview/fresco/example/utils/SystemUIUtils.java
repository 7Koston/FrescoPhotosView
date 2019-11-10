package com.github.koston.photosview.fresco.example.utils;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public final class SystemUIUtils {

  public static void setWindowFlag(AppCompatActivity activity, final int bits, boolean on) {
    Window win = activity.getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }

  public static int calculateSystemUI(
      boolean lightStatusBar,
      boolean lightNavigationBar,
      boolean hideStatusBar,
      boolean hideNavigationBar,
      boolean isMultiWindow) {
    int ui = 0;
    if (Build.VERSION.SDK_INT < 19) {
      ui = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
      if (hideStatusBar) {
        ui = ui | View.SYSTEM_UI_FLAG_FULLSCREEN;
      }
      if (hideNavigationBar) {
        ui = ui | View.SYSTEM_UI_FLAG_LOW_PROFILE;
      }
    } else if (Build.VERSION.SDK_INT < 23) {
      ui =
          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
      if (hideStatusBar) {
        ui = ui | View.SYSTEM_UI_FLAG_FULLSCREEN;
      }
      if (hideNavigationBar) {
        ui = ui | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      }
    } else if (Build.VERSION.SDK_INT < 27) {
      ui = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      if (!isMultiWindow) {
        ui =
            ui | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
      }
      if (hideStatusBar && !isMultiWindow) {
        ui = ui | View.SYSTEM_UI_FLAG_FULLSCREEN;
      }
      if (hideNavigationBar && !isMultiWindow) {
        ui = ui | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      }
      if (lightStatusBar) {
        ui = ui | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
      }
    } else if (Build.VERSION.SDK_INT < 40) {
      ui = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      if (!isMultiWindow) {
        ui =
            ui | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
      }
      if (hideStatusBar && !isMultiWindow) {
        ui = ui | View.SYSTEM_UI_FLAG_FULLSCREEN;
      }
      if (hideNavigationBar && !isMultiWindow) {
        ui = ui | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      }
      if (lightStatusBar && !isMultiWindow) {
        ui = ui | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
      }
      if (lightNavigationBar) {
        ui = ui | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
      }
    }
    return ui;
  }
}
