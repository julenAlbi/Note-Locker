package com.example.julen.nativeappsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.julen.nativeappsproject.model.Note

class NoteActivity : AppCompatActivity() {

    companion object {
        const val ADD_EDIT = "add_edit"
        const val VIEW_NOTE = "view_note"
        const val NOTE_MODE = "note_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_fragment)


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.


            val FragmentToDisplay = if(intent.getStringExtra(NoteActivity.NOTE_MODE).equals(NoteActivity.ADD_EDIT))
                addNoteFragment.newInstance()
            else
                NoteFragment.newInstance(
                        intent.getSerializableExtra(NoteFragment.ARG_NOTE) as Note
                )
            supportFragmentManager.beginTransaction()
                    .add(R.id.noteFrame, FragmentToDisplay)
                    .commit()
        }


    }
}
