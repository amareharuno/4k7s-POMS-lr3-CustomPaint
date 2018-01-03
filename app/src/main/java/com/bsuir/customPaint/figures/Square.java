package com.bsuir.customPaint.figures;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Square extends Figure {

    public Square(PointF origin, int color, float lineWeight) {
        super(origin, color, lineWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(getOrigin().x, getCurrent().x);
        float right = Math.max(getOrigin().x, getCurrent().x);
        float top = Math.min(getOrigin().y, getCurrent().y);
        float bottom = right - left + top;
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getThickness());
        canvas.drawRect(left, top, right, bottom, paint);
    }
}