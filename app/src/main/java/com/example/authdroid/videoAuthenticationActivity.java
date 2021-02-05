package com.example.authdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

public class videoAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_authentication);

        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = getIntent().getData();

        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}