package com.example.jnlycklama.mustela;

import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by jnlycklama on 2016-03-05.
 */
public class OKRequest extends AsyncTask<String, Void, Void> {

    private final OkHttpClient client = new OkHttpClient();

    protected Void doInBackground(String... arg0) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("", "")
                .build();
        Request request = new Request.Builder()
                .url("http://13.67.63.91:8080/:"+arg0[0])
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println("???:"+response.body().string());
        }catch(IOException e){

        }

        return null;
    }

}
