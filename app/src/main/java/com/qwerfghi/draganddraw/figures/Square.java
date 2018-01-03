package com.qwerfghi.draganddraw.figures;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Square extends Figure {

    public Square(PointF origin, int color) {
        super(origin, color);
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(getOrigin().x, getCurrent().x);
        float right = Math.max(getOrigin().x, getCurrent().x);
        float top = Math.min(getOrigin().y, getCurrent().y);
        float bottom = right - left + top;
        Paint paint = new Paint();
        paint.setColor(getColor());
        canvas.drawRect(left, top, right, bottom, paint);
    }
}