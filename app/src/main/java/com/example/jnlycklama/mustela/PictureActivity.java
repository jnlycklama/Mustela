package com.example.jnlycklama.mustela;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); */

        try{
            if (Camera.getNumberOfCameras() >= 2) {

                //if you want to open front facing camera use this line
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

            }
            //mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        //btn to close the application
        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        Button b = (Button) findViewById(R.id.button_pic);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCamera.takePicture(null, null, mPicture);
                Intent intent = new Intent(PictureActivity.this, CompletedActivity.class);
                startActivity(intent);

            }
        });
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                System.out.println("no pic");
                return;
            }
            try {
                System.out.println("woah");
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.d("juliesmells", "jesus pleasus");
                runBlobGettingStartedSample(pictureFile);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    public void runBlobGettingStartedSample(File pic) {
        new BlobUploadTask()
                .execute(pic, null, null);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
               // .format(new Date());
        File mediaFile;
        System.out.println("Path: "+mediaStorageDir.getPath());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + MainActivity.getText() + ".jpg");

        System.out.println("File name"+mediaFile.getName());
        return mediaFile;
    }

    // Define the connection-string with your values
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;AccountName=mustelastorage;AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==;BlobEndpoint=https://mustelastorage.blob.core.windows.net/;TableEndpoint=https://mustelastorage.table.core.windows.net/;QueueEndpoint=https://mustelastorage.queue.core.windows.net/;FileEndpoint=https://mustelastorage.file.core.windows.net/";
            /*"DefaultEndpointsProtocol=http;" +
                    "AccountName=mustelastorage;" +
                    "AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==";*/


}
