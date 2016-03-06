package com.example.jnlycklama.mustela;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class CompletedActivity extends AppCompatActivity {

    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        TextView result = (TextView) findViewById(R.id.result_text);
        Button r_button = (Button) findViewById(R.id.button_home);
        /*final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        int color = 0xFF222222;
        //progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        //progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        //final ImageView videoView =(ImageView)view.findViewById(R.id.video_preview);

//        VideoView videoView =(VideoView)view.findViewById(R.id.video_preview);
//        MediaController mediaController= new MediaController(getActivity());
//        mediaController.setAnchorView(videoView);
//        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.one);
//        videoView.setMediaController(mediaController);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        videoView.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity c = CompletedActivity.this;
                if (c!=null){
                   progressBar.setVisibility(View.INVISIBLE);
                }

            }
        }, 3000);*/

        AsyncTask task;

        if (!LoginCreateActivity.getState()) {
            try {

                String input = "create/" + LoginCreateActivity.getUsername() + "/blob/" + PictureActivity.getBlob();
                System.out.println("First request input: " + input);
                task = new OKRequest()
                        .execute(input, null, null);
            } catch (Exception e) {
                task = null;
            }
            System.out.println("State: " + LoginCreateActivity.getState());
            result.setText("Your account has been created.");
            r_button.setText("Login");
            LoginCreateActivity.setState(true);
        }
        else{
            AsyncTask task2;
            try {
                //String timeStamp = String.valueOf(new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new java.util.Date());
                task2 = new OKRequest()
                        .execute("authenticate/" + LoginCreateActivity.getUsername() + "/blob/" + PictureActivity.getBlob(), null, null);
                System.out.println("hellooooooo");
            } catch (Exception e) {
                e.printStackTrace();
                task2 = null;
            }

            try {
                task2.get(8000, TimeUnit.MILLISECONDS);

            } catch (Exception e) {

            }
            JSONObject y = OKRequest.getResponse();
            boolean z = false;
            try {
                z = (boolean) y.get("isIdentical");
            } catch (JSONException e) {

            }
            if (z) {
                result.setText("Your face matched our records! You are now logged in.");
                r_button.setText("Continue");
            } else {
                result.setText("Your face did not match our records, please try again.");
                r_button.setText("Retry");
            }
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            Intent intent = new Intent(this, MainActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClickBack(View v) {
        // Go! Button Click
       // mEdit  = (EditText)findViewById(R.id.input_username);
        //state = true; // on login account

        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
        //Button button=(Button) v;
        //setContentView(R.layout.activity_picture);
    }

}
