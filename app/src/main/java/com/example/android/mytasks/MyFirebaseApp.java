package com.example.android.mytasks;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by SG on 7/25/2018.
 */

public class MyFirebaseApp extends android.app.Application{

        @Override
        public void onCreate() {
            super.onCreate();
    /* Enable disk persistence  */
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


}