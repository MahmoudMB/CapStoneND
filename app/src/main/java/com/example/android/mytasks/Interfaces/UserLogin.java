package com.example.android.mytasks.Interfaces;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by SG on 7/24/2018.
 */

public interface UserLogin {

    void CheckStatusOfSignUp(boolean status,String Msg);
    void UpdateUiSignIn(FirebaseUser user,String Msg);


}
