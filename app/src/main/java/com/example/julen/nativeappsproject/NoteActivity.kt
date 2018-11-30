package com.example.julen.nativeappsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.julen.nativeappsproject.model.Note

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_fragment)


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            val detailFragment = NoteFragment.newInstance(
                    intent.getSerializableExtra(NoteFragment.ARG_NOTE) as Note
            )

            supportFragmentManager.beginTransaction()
                    .add(R.id.noteFrame, detailFragment)
                    .commit()
        }


    }
}
