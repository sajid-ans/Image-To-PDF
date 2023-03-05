package com.example.imagetopdf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout reduceImage = findViewById(R.id.linearLayout1);
        LinearLayout extractText = findViewById(R.id.linearLayout2);
        LinearLayout imageToPdf = findViewById(R.id.linearLayout3);
        LinearLayout writOnImage = findViewById(R.id.linearLayout4);

        Intent reduceIntent = new Intent(MainActivity.this,ImageActivity.class);
        Intent extractTextIntent = new Intent(MainActivity.this,ExtractTextActivity.class);
        Intent imageToPdfIntent = new Intent(MainActivity.this,ImageToPdfActivity.class);
        Intent writOnImageIntent = new Intent(MainActivity.this,WriImageActivity.class);

        reduceImage.setOnClickListener(v -> {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity(reduceIntent);
            }else{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        });
        extractText.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    startActivity(extractTextIntent);
                }else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }else{
                startActivity(extractTextIntent);
                //Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }
        });
        imageToPdf.setOnClickListener(v->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    startActivity(imageToPdfIntent);
                }else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }else{
                startActivity(imageToPdfIntent);
                //Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }
        });
        writOnImage.setOnClickListener(v->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    startActivity(writOnImageIntent);
                }else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }else{
                startActivity(writOnImageIntent);
                //Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }
        });
        if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Without Permission it will not work", Toast.LENGTH_SHORT).show();
            }
        }
    }
}