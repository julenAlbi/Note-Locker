package com.example.julen.nativeappsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.julen.nativeappsproject.note.NoteActivity
import kotlinx.android.synthetic.main.activity_note_fragment.*

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setSupportActionBar(toolbar_note)

        //For back button
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val FragmentToDisplay = ChangePaswordFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.changePasswordFrame, FragmentToDisplay)
                .commit()
    }

    //For back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
