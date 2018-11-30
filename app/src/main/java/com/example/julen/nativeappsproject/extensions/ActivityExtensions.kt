package com.example.julen.nativeappsproject.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.julen.nativeappsproject.NoteListActivity
import com.example.julen.nativeappsproject.SignUpActivity

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Activity.startHomeActivity() = startActivity(NoteListActivity::class.java)

fun Activity.startSignUpActivity() = startActivity(SignUpActivity::class.java)

private fun Activity.startActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}