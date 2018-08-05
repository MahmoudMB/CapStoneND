package com.example.android.mytasks;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.mytasks.Interfaces.UserLogin;
import com.example.android.mytasks.Models.User;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity implements UserLogin {



    @BindView(R.id.SignUp_Name_EditText) EditText NameText;
    @BindView(R.id.SignUp_Email_EditText) EditText EmailText;
    @BindView(R.id.SignUp_Password_EditText) EditText PasswordText;
    @BindView(R.id.SignUp_VerifyPassword_EditText) EditText VerifyPasswordText;
    @BindView(R.id.SignUp_SignUp_Button) TextView SignUpBtn;




    private String Name;
    private String Email;
    private String Password;
    private String VerifyPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);




    }

    @OnClick(R.id.SignUp_SignUp_Button) void submit() {


        CreateNewUser();
    }

    public void CreateNewUser() {

        Name = NameText.getText().toString();

        Email = EmailText.getText().toString();

        Password = PasswordText.getText().toString();

        VerifyPassword =VerifyPasswordText.getText().toString();


        if (!Name.isEmpty()&&!Email.isEmpty()&&!Password.isEmpty()&&!VerifyPassword.isEmpty()){

            if (!Password.equals(VerifyPassword))
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "The Password and Verify Password Do not Match", Snackbar.LENGTH_LONG);
                snackbar.show();
            }


            else
            {

                findViewById(R.id.prograss_SignUp).setVisibility(View.VISIBLE);
                findViewById(R.id.SignUp_SignUp_Button).setVisibility(View.GONE);
                User user = new User(null,Name,null,Email);
                FirebaseManager.createNewUser(user.getEmail(),Password,user.getName(),user.getPhotoUrl(),this);

            }

        }

        else{

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter All The Fields", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }




    @Override
    public void CheckStatusOfSignUp(boolean status,String Msg) {


        if (status)
        {

            Intent i = new Intent(SignupActivity.this,MainScreen.class);
            startActivity(i);
            finish();
        }

        else if (!status)

        {
            findViewById(R.id.prograss_SignUp).setVisibility(View.GONE);
            findViewById(R.id.SignUp_SignUp_Button).setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }

    @Override
    public void UpdateUiSignIn(FirebaseUser user,String Msg) {

    }

}
