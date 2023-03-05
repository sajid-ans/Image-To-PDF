package com.example.imagetopdf;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageActivity extends AppCompatActivity {
    static final int READ_REQUEST_CODE = 42;
    String finalfilePath;
    ImageView ornlImage,cngImage;
    TextView orgnlSize,cngSize;
    File originalImage;
    Uri uri;
    Bitmap finalReduceImage;
    int height=0,weight=0;
    EditText txtHeight,txtWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        LinearLayout btnPic = findViewById(R.id.btnUpload);
        LinearLayout btnCompress = findViewById(R.id.btnCompress);
        ornlImage = findViewById(R.id.orignalImage);
        cngImage = findViewById(R.id.changeImage);
        orgnlSize = findViewById(R.id.txtOriginal);
        cngSize = findViewById(R.id.txtChange);
        LinearLayout btnSave = findViewById(R.id.btnSave);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        btnPic.setOnClickListener(view ->performFileSearch());
        btnCompress.setOnClickListener(v -> {
            try {
                height = Integer.parseInt(txtHeight.getText().toString());
                weight = Integer.parseInt(txtWeight.getText().toString());
                if(finalfilePath==null){
                    Toast.makeText(this, "First Upload Image", Toast.LENGTH_SHORT).show();
                }else{
                    finalReduceImage = reduceImageSize(finalfilePath,height,weight);
                    cngImage.setImageBitmap(finalReduceImage);
                    cngSize.setText("Size " + Formatter.formatShortFileSize(getApplicationContext(),finalReduceImage.getByteCount()));
                }
            }catch (NumberFormatException e){
                Toast.makeText(this, "First Enter Height and Weight", Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(v -> saveImageToGallery(finalReduceImage));

    }
    public void performFileSearch() {
        // Create an Intent to request the user to select a file
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The result data contains the URI of the selected file
            if (resultData != null) {
                uri = resultData.getData();
                finalfilePath = getPathFromUri(uri);
                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ornlImage.setImageBitmap(selectedImage);
                    originalImage = new File(uri.getPath().replace("raw/",""));
                    orgnlSize.setText("size" + Formatter.formatShortFileSize(this,originalImage.length()));

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
            image.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            Toast.makeText(this, "successfully to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "failed to save image in gallery", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    public Bitmap reduceImageSize(String filePath, int targetHeight, int targetWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int photoWidth = options.outWidth;
        int photoHeight = options.outHeight;

        int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        return BitmapFactory.decodeFile(filePath, options);
    }

}