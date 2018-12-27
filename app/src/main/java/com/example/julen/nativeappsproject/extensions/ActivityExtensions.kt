package com.example.julen.nativeappsproject.extensions

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.Toast
import com.example.julen.nativeappsproject.note.NoteActivity
import com.example.julen.nativeappsproject.note.NoteFragment
import com.example.julen.nativeappsproject.note.NoteListActivity
import com.example.julen.nativeappsproject.SignUpActivity
import com.example.julen.nativeappsproject.model.Note

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Activity.startHomeActivity() = startActivity(NoteListActivity::class.java, clearTask = true)

fun Activity.startSignUpActivity() = startActivity(SignUpActivity::class.java, clearTask = true)

fun Activity.startNoteActivity(note : Note? = null, mode : String){
    var notee = if(mode.equals(NoteActivity.ADD_NOTE)) Note(0,"","") else note
    startActivity(NoteActivity::class.java, notee, mode)
}

private fun Activity.startActivity(cls: Class<*>, note : Note? = null, mode: String? = null, clearTask: Boolean = false) {
    val intent = Intent(this, cls).apply {
        note?.let {
            putExtra(NoteFragment.ARG_NOTE, it)
        }
        mode?.let {
            putExtra(NoteActivity.NOTE_MODE, it)
        }
    }
    if(clearTask) intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK).addFlags(FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}