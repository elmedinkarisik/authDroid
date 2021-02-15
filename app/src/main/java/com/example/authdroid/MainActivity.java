package com.example.authdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authdroid.util.Base64;
import com.example.authdroid.util.SOAPClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    /**
     * Tag used for logging
     */
    public static final String LOG_TAG = "DroidAuth";

    /**
     * Original file name
     */
    private String orgImgName;

    private String orgVideoName;
    /**
     * Original image file
     */
    private File originalFile;

    private File originalVideo;

    /**
     * Bitmap options
     */
    private BitmapFactory.Options bmpOpt;

    /**
     * Downsampling rate for every captured image;
     */
    private Integer downSampleRate;

    /**
     * A downsampled and compressed bitmap.
     */
    protected Bitmap mBitmap;

    /**
     * Image capturing intent result.
     */
    private static final int RESULT_CAPTURE_IMAGE = 0;

    /**
     * Sound recording intent result.
     */
    private static final int RESULT_RECORD_VIDEO = 1;

    /**
     * Simple SOAP client.
     */
    private SOAPClient client;

    /**
     * Service endpoint.
     */
    private String serviceEndpoint;

    /**
     * Euclid tolerance
     */
    private String euclidTolerance;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int CAMERA_REQUEST = 3;

    Button imageButton;
    Button videoButton;
    FloatingActionButton cameraButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.authWithImageButton);
        videoButton = findViewById(R.id.authWithVideoButton);
        cameraButton = findViewById(R.id.authWithCameraButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                /**
                 * In original app:
                 */
                //useCamera(); // TODO: Uncomment to test on real device...
                //fakeProcessing();
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_VIDEO_REQUEST);
                /**
                 * In original app:
                 */
                //useCameraVideo();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
    }

    /**
     * Called when activity is started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    /**
     * In original app:
     */
    /**
     * Called after each intent with result exits. Used for camera and audio
     * recording intents.
     */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Perform validation...
        if (serviceEndpoint == null || "".equals(serviceEndpoint)) {
            Log.d(LOG_TAG, "Missing service endpoint setting value...");
            Toast.makeText(this, "Missing service endpoint setting value...", Toast.LENGTH_LONG).show();
            return;
        } else if (euclidTolerance == null || "".equals(euclidTolerance)) {
            Log.d(LOG_TAG, "Missing euclid preference value...");
            Toast.makeText(this, "Missing euclid setting value...", Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == RESULT_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {

            *//*if (originalFile == null || !originalFile.exists()) {
                Log.d(LOG_TAG, "Missing captured file...");
                Toast.makeText(this, "Missing captured image file...", Toast.LENGTH_LONG).show();
                return;
            }*//*

            mBitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bmpOpt);
            ImageView capImgView = (ImageView) findViewById(R.id.yourImageImageView);
            capImgView.setImageBitmap(mBitmap);
            capImgView.refreshDrawableState();
            // capImgView.invalidate();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, bao);
            byte[] data = bao.toByteArray();
            String imgBase64 = Base64.encodeBytes(data);// Image encoded as Base
            // 64 string :(

            String[] fileMeta = originalFile.getName().split("\\.");
            String imgName = fileMeta[0];
            String imgType = fileMeta[1];

            client = new SOAPClient(serviceEndpoint);
            client.upload(imgBase64, imgName, imgType);
            Boolean recognized = client.authenticate(euclidTolerance, originalFile.getName());
            showRecognitionStatus(recognized);

            return;

        } else if (requestCode == RESULT_RECORD_VIDEO && resultCode == Activity.RESULT_OK) {
            try {
                String path = getRealPathFromURI(intent.getData());
                File videoFile = new File(path);
                FileInputStream stream = new FileInputStream(videoFile);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];

                while (stream.read(buffer) > -1) {
                    baos.write(buffer);
                }

                stream.close();
                byte[] data = baos.toByteArray();
                String imgBase64 = Base64.encodeBytes(data);

                String[] fileMeta = orgVideoName.split("\\.");
                String imgName = fileMeta[0];
                String imgType = fileMeta[1];

                client = new SOAPClient(serviceEndpoint);
                client.upload(imgBase64, imgName, imgType);
                Boolean recognized = client.authenticate(euclidTolerance, orgVideoName);
                showRecognitionStatus(recognized);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }*/

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap camera = null;

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Intent intent = new Intent(MainActivity.this, photoAuthenticationActivity.class);
            intent.setData(selectedImage);
            startActivity(intent);
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Intent intent = new Intent(MainActivity.this, videoAuthenticationActivity.class);
            intent.setData(selectedVideo);
            startActivity(intent);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && null != data) {
            camera = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(MainActivity.this, photoAuthenticationActivity.class);
            intent.putExtra("image", camera);
            startActivity(intent);
        }
    }

    /**
     * Generates custom options menu from menu resource file.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settingsMenuItem: {
                Intent setActivityIntent = new Intent(MainActivity.this, AppPreferenceActivity.class);
                startActivity(setActivityIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Launch the camera intent to take a new picture and store it in defined
     * path.
     */
    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputFileUri = Uri.fromFile(originalFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Log.v(LOG_TAG, "gotoCamera - Intent imageCaptureIntent created");
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

    private void useCameraVideo() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        startActivityForResult(intent, 1);
    }

    /**
     * Shows authentication status on UI.
     *
     * @param recognized
     */
    private void showRecognitionStatus(Boolean recognized) {
        TextView stat = (TextView) findViewById(R.id.chooseAuthMethodTextView);
        if (recognized == null) {
            stat.setText(R.string.statusUnknown);
            stat.setTextColor(Color.WHITE);
            return;
        }

        if (recognized) {
            stat.setText(R.string.statusSucessfull);
            stat.setTextColor(Color.parseColor("#B2D740"));
        } else {
            stat.setText(R.string.statusFail);
            stat.setTextColor(Color.parseColor("#E02629"));
        }
    }

    private void loadPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        serviceEndpoint = preferences.getString("engine.endpoint", null);
        Log.d("Preference-change", "serviceEndpoint:" + serviceEndpoint);
        downSampleRate = Integer.parseInt(preferences.getString("downsample.rate", "1"));
        Log.d("Preference-change", "downSampleRate:" + downSampleRate);
        orgImgName = preferences.getString("capture.image.name", null);
        orgVideoName = preferences.getString("capture.video.name", null);
        Log.d("Preference-change", "orgImgName:" + orgImgName);
        euclidTolerance = preferences.getString("euclid.tolerance", null);
        Log.d("Preference-change", "euclidTolerance:" + euclidTolerance);

        if (downSampleRate != null && !"".equals(downSampleRate)) {
            bmpOpt = new BitmapFactory.Options();
            bmpOpt.inSampleSize = downSampleRate;
        }
        if (orgImgName != null && !"".equals(orgImgName)) {
            originalFile = new File(Environment.getExternalStorageDirectory(), orgImgName);
            originalFile.deleteOnExit();
        }

        if (orgVideoName != null && !"".equals(orgVideoName)) {
            originalVideo = new File(Environment.getExternalStorageDirectory(), orgVideoName);
            originalVideo.deleteOnExit();
        }
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}