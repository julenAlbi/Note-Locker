package com.example.julen.nativeappsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.activity_note_fragment.*

class NoteActivity : AppCompatActivity(), FragmentCommunication  {
    override fun changeFragment(note : Note?) {

        if(intent.getStringExtra(NoteActivity.NOTE_MODE).equals(NoteActivity.ADD_EDIT)){
            intent.putExtra(NoteActivity.NOTE_MODE,NoteActivity.VIEW_NOTE)

        }else{
            intent.putExtra(NoteActivity.NOTE_MODE,NoteActivity.ADD_EDIT)
        }
        val FragmentToDisplay = if(intent.getStringExtra(NoteActivity.NOTE_MODE).equals(NoteActivity.ADD_EDIT))
            AddNoteFragment.newInstance(note)
        else
            NoteFragment.newInstance(note!!)
        supportFragmentManager.beginTransaction()
                .replace(R.id.noteFrame, FragmentToDisplay)
                .commit()
        invalidateOptionsMenu()
    }

    companion object {
        const val ADD_EDIT = "add_edit"
        const val VIEW_NOTE = "view_note"
        const val NOTE_MODE = "note_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_fragment)

        setSupportActionBar(toolbar_note)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.


            val FragmentToDisplay = if(intent.getStringExtra(NoteActivity.NOTE_MODE).equals(NoteActivity.ADD_EDIT))
                AddNoteFragment.newInstance()
            else
                NoteFragment.newInstance(
                        intent.getSerializableExtra(NoteFragment.ARG_NOTE) as Note
                )
            supportFragmentManager.beginTransaction()
                    .add(R.id.noteFrame, FragmentToDisplay)
                    .commit()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        Log.d("Menuen", "Honea sartu da.")
        if(intent.getStringExtra(NoteActivity.NOTE_MODE).equals(NoteActivity.ADD_EDIT)){
            menu?.findItem(R.id.action_save)?.isVisible = true
            menu?.findItem(R.id.action_edit)?.isVisible = false
        }else{
            menu?.findItem(R.id.action_edit)?.isVisible = true
            menu?.findItem(R.id.action_save)?.isVisible = false
        }
        return true
    }
}
