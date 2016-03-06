package com.example.jnlycklama.mustela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class CompletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        try {

            String input = "create/"+LoginCreateActivity.getUsername()+"/blob/"+PictureActivity.getBlob();
            System.out.println("First request input: "+input);
            new OKRequest()
                    .execute(input, null, null);
        }catch (Exception e){

        }
        System.out.println("State: "+LoginCreateActivity.getState());
        if (LoginCreateActivity.getState()){
            try {
                //String timeStamp = String.valueOf(new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new java.util.Date());
                new OKRequest()
                        .execute("authenticate/" + LoginCreateActivity.getUsername() + "/blob/"+PictureActivity.getBlob(), null, null);
                System.out.println("hellooooooo");
            }catch (Exception e){
                e.printStackTrace();
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

}
