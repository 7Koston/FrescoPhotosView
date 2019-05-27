package com.github.koston.photosview.fresco.example;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.github.koston.photosview.fresco.example.utils.ArcUtils;

public class CircleProgressBarDrawable extends ProgressBarDrawable {
  private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private int mLevel = 0;
  private int maxLevel = 10000;

  @Override
  protected boolean onLevelChange(int level) {
    mLevel = level;
    invalidateSelf();
    return true;
  }

  @Override
  public void draw(Canvas canvas) {
    if (getHideWhenZero() && mLevel == 0) {
      return;
    }
    //if (mLevel < maxLevel / 8) drawBar(canvas, maxLevel / 8, Color.BLUE);
    drawBar(canvas, mLevel, Color.BLUE);
  }

  private void drawBar(Canvas canvas, int level, int color) {
    Rect bounds = getBounds();

    float x = bounds.right / 2f;
    float y = bounds.bottom / 2f;
    PointF center = new PointF(x, y);

    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(2);
    if (level != 0) {
      ArcUtils.drawArc(canvas, center, 50, 0, (float) (level * 360 / maxLevel), mPaint);
    } else {
      ArcUtils.drawArc(canvas, center, 50, 0, (float) (400 * 360 / maxLevel), mPaint);
    }
  }
}
