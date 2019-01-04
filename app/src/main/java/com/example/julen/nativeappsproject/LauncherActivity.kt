package com.example.julen.nativeappsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.julen.nativeappsproject.extensions.startHomeActivity
import com.example.julen.nativeappsproject.extensions.startSignUpActivity
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager

/**
 * At the start of the app, this activity will be launched.
 * This activity verifies if the user is signed up.
 * If the user is signed up, it will launch the note list activity,
 * else it will launch the sign up activity.
 */
class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        if (SharedPreferencesManager(this).isPasswordSaved())
            startHomeActivity()
        else
            startSignUpActivity()
    }
}
