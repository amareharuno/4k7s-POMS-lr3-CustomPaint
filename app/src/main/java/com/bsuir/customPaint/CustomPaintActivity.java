package com.bsuir.customPaint;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bsuir.customPaint.figures.Figure;
import com.bsuir.customPaint.figures.FigureType;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomPaintActivity extends AppCompatActivity {
    private DrawingView mDrawingView;
    private Button mCircleButton;
    private Button mRectangleButton;
    private Button mSquareButton;
    private Button mOvalButton;
    private Button mLineButton;
    private Button mLineColorButton;
    private SeekBar mLineWeightBar;
    private Button mClearButton;
    private Button mBackButton;
    private Button mSaveButton;
    private int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        mDrawingView = findViewById(R.id.drawing_view);
        mDrawingView.setDrawingCacheEnabled(true);
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
        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(view -> {
            List<Figure> figures = mDrawingView.getFigures();
            if (figures.size() != 0) {
                figures.remove(figures.size() - 1);
            }
            mDrawingView.invalidate();
        });
        mClearButton = findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(view -> {
            mDrawingView.getFigures().clear();
            mDrawingView.invalidate();
        });
        mSaveButton = findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(view -> {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermission();
            } else {
                saveImage();
            }
        });
        mLineColorButton = findViewById(R.id.line_color_button);
        mLineColorButton.setOnClickListener(
                view -> ColorPickerDialogBuilder
                        .with(CustomPaintActivity.this)
                        .setTitle("Choose color")
                        .initialColor(mDrawingView.getFigurePaint().getColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setPositiveButton(
                                "OK",
                                (dialog, selectedColor, allColors) -> mDrawingView.getFigurePaint().setColor(selectedColor))
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .build()
                        .show());
        mLineWeightBar = findViewById(R.id.line_weight_bar);
        mLineWeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mDrawingView.getFigurePaint().setStrokeWidth(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(),
//                            getResources().getString(R.string.permission_storage_success),
//                            Toast.LENGTH_SHORT).show();
                    saveImage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.permission_storage_failure),
                            Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    private void saveImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(imageFileName, ".jpeg", storageDir);
            if (mDrawingView.getDrawingCache() != null) {
                Log.d("log", mDrawingView.getDrawingCache().toString());
            }
            mDrawingView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(image));
            Toast.makeText(getApplicationContext(), "image saved to: " + storageDir, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}