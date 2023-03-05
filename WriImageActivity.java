package com.example.imagetopdf;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;

public class WriImageActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    Paint paint ;
    private float startX = -1;
    private float startY = -1;
    private ImageView mImageView;
    private Canvas canvas;
    Bitmap originalBitmap,mutableBitmap;
    Button upload,save;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wri_image);

        mImageView = findViewById(R.id.imageView);
        upload = findViewById(R.id.btnUpload);
        save = findViewById(R.id.btnSave);

        // Start the gallery to pick an image
        upload.setOnClickListener(v -> openGallery());
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        save.setOnClickListener(v -> saveImageToGallery(mutableBitmap));

    }
    private void saveImageToGallery(Bitmap image) {
        // Create a content values object where we specify the values we want to insert.
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "MyImage");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Get a URL for the directory in the media store where we want to insert the image.
        Uri directory = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Insert the image into the media store.
        Uri imageUri = getContentResolver().insert(directory, values);

        // Open an output stream to the newly created image URI.
        OutputStream out;
        try {
            out = getContentResolver().openOutputStream(imageUri);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, "successfully to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "failed to save image in gallery", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            try {
                originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                canvas = new Canvas(mutableBitmap);
                mImageView.setImageBitmap(mutableBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                canvas.drawLine(startX, startY-220, endX, endY -220, paint);
                mImageView.invalidate();
                startX = endX;
                startY = endY;
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                canvas.drawLine(startX,startY-220, endX, endY -220,paint);
                mImageView.invalidate();
                break;
        }
        return true;
    }
}