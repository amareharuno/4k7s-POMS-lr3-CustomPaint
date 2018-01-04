package com.bsuir.customPaint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bsuir.customPaint.figures.Figure;
import com.bsuir.customPaint.figures.FigureType;
import com.bsuir.customPaint.figures.Picture;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private Button mOpenButton;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE = 3;

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
                requestWritePermission();
            } else {
                saveImage();
            }
        });
        mOpenButton = findViewById(R.id.open_button);
        mOpenButton.setOnClickListener(view -> {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestReadPermission();
            } else {
                openImage();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mDrawingView.getFigures().add(new Picture(selectedImage));
                        mDrawingView.invalidate();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.permission_storage_failure),
                            Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.permission_storage_failure),
                            Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            }
        }
    }

    private void requestWritePermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    private void requestReadPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void saveImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(imageFileName, ".jpeg", storageDir);
            viewToBitmap(mDrawingView).compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(image));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(image);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            Toast.makeText(getApplicationContext(), "image saved to: " + storageDir, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    private Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}