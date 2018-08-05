package com.example.android.mytasks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mytasks.Interfaces.UserLogin;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class SigninActivity extends AppCompatActivity implements View.OnClickListener,UserLogin {

    private FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;



    @BindView(R.id.SignIn_Email) EditText EmailText;
    @BindView(R.id.Sign_In_password) EditText PasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);
        mFireBaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null)
                {
                    Intent i = new Intent(SigninActivity.this,MainScreen.class);
                    startActivity(i);

                }

            }

        };

    }
    @Override
    protected void onResume() {
        super.onResume();
        mFireBaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener!=null)
            mFireBaseAuth.removeAuthStateListener(mAuthStateListener);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthStateListener!=null)
            mFireBaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View view) {

    }

    @OnClick(R.id.Login_signin) void SignInBtn() {


        if (!EmailText.getText().toString().isEmpty() && !PasswordText.getText().toString().isEmpty())
        {
            findViewById(R.id.prograss_SignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.Login_signin).setVisibility(View.GONE);


            SignIn(EmailText.getText().toString(),PasswordText.getText().toString());

        }
        else
        {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter The Email And Password", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }



    @OnClick(R.id.Login_Rigister) void Rigister(){
        Intent i = new Intent(SigninActivity.this,SignupActivity.class);
        startActivity(i);
    }

    public void SignIn(String Email,String Password)
    {

        FirebaseManager.SignIn(Email,Password,this);
    }




    @Override
    public void CheckStatusOfSignUp(boolean status,String Msg) {

    }

    @Override
    public void UpdateUiSignIn(FirebaseUser user,String Msg) {


        if (user!=null)
        {
            findViewById(R.id.prograss_SignIn).setVisibility(View.GONE);

            Intent i = new Intent(SigninActivity.this,MainScreen.class);
            startActivity(i);
            finish();

        }
        else
        {
            findViewById(R.id.prograss_SignIn).setVisibility(View.GONE);
            findViewById(R.id.Login_signin).setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }






}
