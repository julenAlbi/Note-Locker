package com.example.julen.nativeappsproject

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import com.example.julen.nativeappsproject.model.Note

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import org.joda.time.DateTime
import java.util.*

class NoteListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private var notes: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        addSecretButton.setOnClickListener { view ->
            TODO("Add new note.")
        }

        notes = getFakeNotes()

        toolbar.title = title
        note_list.adapter = NoteListAdapter(this, notes!!, twoPane)
        note_list.layoutManager = LinearLayoutManager(this)

    }

    private fun getFakeNotes(): List<Note>? {
        val noteList = mutableListOf<Note>()
        noteList.add(Note("pasahitzak", "iupsiub:fudayby", DateTime.now(), DateTime.now(), true))
        noteList.add(Note("sekretuek", "ouygouydvsoytvsodvtsdp", DateTime.now(), DateTime.now(), true))
        noteList.add(Note("beste sekretue", "byfofue fdouyav wpuey", DateTime.now(), DateTime.now(), false))
        return noteList
    }

}
