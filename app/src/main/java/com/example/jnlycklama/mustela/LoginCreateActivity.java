package com.example.jnlycklama.mustela;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class LoginCreateActivity extends AppCompatActivity {

    public static EditText mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


    }

    public static String getUsername(){
        return mEdit.getText().toString();
    }

    public void onClickCreate(View v) {
        // Go! Button Click
        mEdit  = (EditText)findViewById(R.id.input_username);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(this.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
        //Button button=(Button) v;
        //setContentView(R.layout.activity_picture);
    }

    public void onClickLogin(View v) {
        // Go! Button Click
        //mEdit  = (EditText)findViewById(R.id.usernameField);

        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
        //Button button=(Button) v;
        //setContentView(R.layout.activity_picture);
    }

}
