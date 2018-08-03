package com.example.android.mytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.mytasks.Models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


  //  @BindView(R.id.signUpBtn)
  //  TextView SignUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ButterKnife.bind(this);
    }
/*
    @OnClick(R.id.signin) void SignIn() {
        Intent i = new Intent(MainActivity.this,signinActivity.class);
      startActivity(i);
    }

    @OnClick(R.id.signUpBtn) void SignUp() {
        Intent i = new Intent(MainActivity.this,signupActivity.class);
        startActivity(i);
    }*/

}
