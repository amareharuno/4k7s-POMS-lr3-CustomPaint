package com.bsuir.customPaint.figures;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Circle extends Figure {

    public Circle(PointF origin, int color, float lineWeight) {
        super(origin, color, lineWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float x1 = getOrigin().x;
        float x2 = getCurrent().x;
        float y1 = getOrigin().y;
        float y2 = getCurrent().y;
        double radius = Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getThickness());
        canvas.drawCircle(getOrigin().x, getOrigin().y, (float) radius, paint);
    }
}