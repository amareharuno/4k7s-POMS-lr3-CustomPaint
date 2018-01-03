package com.qwerfghi.draganddraw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.qwerfghi.draganddraw.figures.FigureType;

public class DragAndDrawActivity extends AppCompatActivity {
    private DrawingView mDrawingView;
    private Button mCircleButton;
    private Button mRectangleButton;
    private Button mSquareButton;
    private Button mOvalButton;
    private Button mLineButton;
    private Button mLineColorButton;
    private SeekBar mLineSickBar;
    private TextView mASd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        mDrawingView = findViewById(R.id.drawing_view);
        mCircleButton = findViewById(R.id.circle_button);
        mCircleButton.setOnClickListener(view -> mDrawingView.setFigureType(FigureType.CIRCLE));
        mRectangleButton = findViewById(R.id.rectangle_button);
        mRectangleButton.setOnClickListener(view -> mDrawingView.setFigureType(FigureType.RECTANGLE));
        mOvalButton = findViewById(R.id.oval_button);
        mOvalButton.setOnClickListener(view -> mDrawingView.setFigureType(FigureType.OVAL));
        mSquareButton = findViewById(R.id.square_button);
        mSquareButton.setOnClickListener(view -> mDrawingView.setFigureType(FigureType.SQUARE));
        mLineButton = findViewById(R.id.line_button);
        mLineButton.setOnClickListener(view -> mDrawingView.setFigureType(FigureType.LINE));
        mLineColorButton = findViewById(R.id.line_color_button);
        mLineColorButton.setOnClickListener(view -> {
            ColorPickerDialogBuilder
                    .with(DragAndDrawActivity.this)
                    .setTitle("Choose color")
                    .initialColor(mDrawingView.getFigurePaint().getColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("OK", (dialog, selectedColor, allColors) -> {
                        mDrawingView.getFigurePaint().setColor(selectedColor);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mASd = findViewById(R.id.hui);
        mLineSickBar = findViewById(R.id.line_thickness_bar);
        mLineSickBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mASd.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}