package com.qwerfghi.draganddraw.figures;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Oval extends Figure {
    public Oval(PointF origin, int color) {
        super(origin, color);
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(this.getOrigin().x, this.getCurrent().x);
        float right = Math.max(this.getOrigin().x, this.getCurrent().x);
        float top = Math.min(this.getOrigin().y, this.getCurrent().y);
        float bottom = Math.max(this.getOrigin().y, this.getCurrent().y);
        Paint paint = new Paint();
        paint.setColor(getColor());
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawOval(rectF, paint);
    }
}