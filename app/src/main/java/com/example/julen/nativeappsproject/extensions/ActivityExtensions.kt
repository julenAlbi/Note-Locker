package com.example.julen.nativeappsproject.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.julen.nativeappsproject.note.NoteActivity
import com.example.julen.nativeappsproject.note.NoteFragment
import com.example.julen.nativeappsproject.note.NoteListActivity
import com.example.julen.nativeappsproject.SignUpActivity
import com.example.julen.nativeappsproject.model.Note

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Activity.startHomeActivity() = startActivity(NoteListActivity::class.java)

fun Activity.startSignUpActivity() = startActivity(SignUpActivity::class.java)

fun Activity.startNoteActivity(note : Note? = null, mode : String, add : String = ""){
    var notee = if(add.equals(NoteActivity.ADD)) Note(0,"","") else note
    startActivity(NoteActivity::class.java, notee, mode)
}

private fun Activity.startActivity(cls: Class<*>, note : Note? = null, mode: String? = null) {
    val intent = Intent(this, cls).apply {
        note?.let {
            putExtra(NoteFragment.ARG_NOTE, it)
        }
        mode?.let {
            putExtra(NoteActivity.NOTE_MODE, it)
        }
    }
    startActivity(intent)
}