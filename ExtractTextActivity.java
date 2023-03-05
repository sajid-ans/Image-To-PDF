package com.example.imagetopdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ExtractTextActivity extends AppCompatActivity {

    ImageView imageview1;
    String uriString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_text);

        Button upload = findViewById(R.id.btnUpload);
        Button extract = findViewById(R.id.btnExtract);
        imageview1 = findViewById(R.id.imageView);

        upload.setOnClickListener(v -> mGetContent.launch("image/*"));

        extract.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("uri", uriString);
            startActivity(intent);
        });
    }
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageview1.setImageURI(uri);
                    uriString = uri.toString();
                }
            });
}