package com.example.julen.nativeappsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.julen.nativeappsproject.extensions.startHomeActivity
import com.example.julen.nativeappsproject.extensions.startSignUpActivity
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager

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
