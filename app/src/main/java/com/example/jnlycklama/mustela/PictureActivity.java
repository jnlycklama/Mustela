package com.example.jnlycklama.mustela;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PictureActivity extends AppCompatActivity  {

    private Camera c = null;
    private CameraView mCameraView = null;
    private Context context = this;
    public static String name_two;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Zimbabwe");
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second


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
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);


            }
            else {
                try {
                    // attempt to get a Front Camera instance
                    c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out
                            .println("fail to connect to Front Camera");
                }
                if (c == null) {
                    try {
                        // attempt to get a Back Camera instance
                        c = Camera.open(1);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out
                                .println("fail to connect to Camera   with      id   =   1");
                    }
                }
                if (c == null) {
                    try {
                        // attempt to get a Back Camera instance
                        c = Camera.open(0);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out
                                .println("fail to connect to Camera   with      id   =   0");
                    }
                }
                if (c == null) {
                    try {
                        // attempt to get a Back Camera instance
                        c = Camera.open();
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out
                                .println("fail to connect to any camera");
                    }
                }
            }
            //mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(c != null) {
            mCameraView = new CameraView(this, c);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout

        }
        else{
            Log.d("I THINK", "NOOOOOOOO");
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

                c.takePicture(null, null, mPicture);

            }
        });
    }


    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bfo.inScaled = false;
            bfo.inDither = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bfo);
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic, bfo);
            if ( bitmap == null) {
                System.out.println("no pic");
                return;
            }
            try {
                int h = rotatedBitmap.getHeight();
                int w = rotatedBitmap.getWidth();
                int max = 10;

                FaceDetector detector = new FaceDetector(w, h, max);
                android.media.FaceDetector.Face[] faces = new android.media.FaceDetector.Face[max];

                int facesFound = detector.findFaces(rotatedBitmap, faces);
                System.out.println(facesFound + " LOOOOOOOOOOOOOOOOOOOOOOK HEEEEEEEEEEEEEERE");
                System.out.println("woah");
                if(facesFound < 1){
                    Toast.makeText(context, "No Face Detected, Try Again.", Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    camera.release();
                    startActivity(intent);
                }
                else if (facesFound > 1){
                    Toast.makeText(context, "Multiple Faces Detected, Try Again.", Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    camera.release();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG);

                    OutputStream fos;
                    fos = new FileOutputStream(pictureFile);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                    //fos.write(data);
                    fos.flush();
                    fos.close();
                    Log.d("juliesmells", "jesus pleasus");
                    runBlobGettingStartedSample(pictureFile);
                    Intent intent = new Intent(PictureActivity.this, CompletedActivity.class);
                    startActivity(intent);
                }

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
        String timeStamp = String.valueOf(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        System.out.println(timeStamp);
        File mediaFile;
        System.out.println("Path: "+mediaStorageDir.getPath());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + LoginCreateActivity.getUsername() + timeStamp + ".jpg");

        name_two = mediaFile.getName();
        System.out.println("File name" + mediaFile.getName());
        return mediaFile;
    }

    String name;
    public static String getBlob(){
        return name_two;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            if(c!=null) {
                c.release();
            }
            Intent intent = new Intent(this, MainActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (c != null) {
            c = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    
    // Define the connection-string with your values
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;AccountName=mustelastorage;AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==;BlobEndpoint=https://mustelastorage.blob.core.windows.net/;TableEndpoint=https://mustelastorage.table.core.windows.net/;QueueEndpoint=https://mustelastorage.queue.core.windows.net/;FileEndpoint=https://mustelastorage.file.core.windows.net/";
            /*"DefaultEndpointsProtocol=http;" +
                    "AccountName=mustelastorage;" +
                    "AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==";*/


}
