package com.bsuir.customPaint.figures;

import android.graphics.Canvas;
import android.graphics.PointF;

public abstract class Figure {
    private int mColor;
    private float mLineWeight;
    private PointF mOrigin;
    private PointF mCurrent;

    public Figure(PointF origin, int color, float lineWeight) {
        mOrigin = origin;
        mCurrent = origin;
        mColor = color;
        mLineWeight = lineWeight;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public abstract void draw(Canvas canvas);

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getThickness() {
        return mLineWeight;
    }
}