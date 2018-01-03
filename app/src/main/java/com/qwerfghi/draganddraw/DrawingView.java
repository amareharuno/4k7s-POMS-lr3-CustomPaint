package com.qwerfghi.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qwerfghi.draganddraw.figures.Circle;
import com.qwerfghi.draganddraw.figures.Figure;
import com.qwerfghi.draganddraw.figures.FigureType;
import com.qwerfghi.draganddraw.figures.Line;
import com.qwerfghi.draganddraw.figures.Oval;
import com.qwerfghi.draganddraw.figures.Rectangle;
import com.qwerfghi.draganddraw.figures.Square;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    private Figure mCurrentFigure;
    private FigureType mFigureType;
    private List<Figure> mFigures = new ArrayList<>();
    private Paint mFigurePaint;
    private Paint mBackgroundPaint;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFigurePaint = new Paint();
        mFigurePaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mFigureType = FigureType.RECTANGLE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentFigure = getFigureByFigureType(current);
                mFigures.add(mCurrentFigure);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentFigure != null) {
                    mCurrentFigure.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentFigure = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentFigure = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Figure figure : mFigures) {
            figure.draw(canvas);
        }
    }

    public void setFigureType(FigureType figureType) {
        mFigureType = figureType;
    }

    private Figure getFigureByFigureType(PointF current) {
        switch (mFigureType) {
            case CIRCLE:
                return new Circle(current, mFigurePaint.getColor());
            case RECTANGLE:
                return new Rectangle(current, mFigurePaint.getColor());
            case OVAL:
                return new Oval(current, mFigurePaint.getColor());
            case SQUARE:
                return new Square(current, mFigurePaint.getColor());
            case LINE:
                return new Line(current, mFigurePaint.getColor());
            default:
                return null;
        }
    }

    public Paint getFigurePaint() {
        return mFigurePaint;
    }
}