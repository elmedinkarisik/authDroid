package com.example.authdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class photoAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_authentication);

        ImageView imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bitmap image = (Bitmap) extras.get("image");
            if (image != null) {
                imageView.setImageBitmap(image);
//                imageurl = getRealPathFromURI(imageUri);
            }
        }

        Uri imageUri = getIntent().getData();
        if (imageUri != null) {
            imageView.setImageURI(imageUri);
        }
    }
}