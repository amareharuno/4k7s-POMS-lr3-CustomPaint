package com.qwerfghi.draganddraw.figures;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Line extends Figure {

    public Line(PointF origin, int color) {
        super(origin, color);
    }

    @Override
    public void draw(Canvas canvas) {
        float x1 = getOrigin().x;
        float x2 = getCurrent().x;
        float y1 = getOrigin().y;
        float y2 = getCurrent().y;
        Paint paint = new Paint();
        paint.setColor(getColor());
        canvas.drawLine(x1, y1, x2, y2, paint);
    }
}