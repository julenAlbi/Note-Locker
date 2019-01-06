package com.example.julen.nativeappsproject.note

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.SignUpActivity
import org.joda.time.DateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NoteActivityTest(){

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SignUpActivity::class.java)

    @Test
    fun test_note() {

        //sign up
        val password = "123"
        onView(withId(R.id.passwordText)).perform(typeText(password))
        onView(withId(R.id.confirmpasswordText)).perform(typeText(password))
        onView(withId(R.id.signup_button)).perform(click())

        //Create a note
        val alias = "Testing"
        val note = "This is a testing note."
        onView(withId(R.id.addSecretButton)).perform(click())
        onView(withId(R.id.noteName)).perform(typeText(alias))
        onView(withId(R.id.lockedCheckBox)).perform(click())
        onView(withId(R.id.noteTextEditing)).perform((typeText(note)))
        onView(withId(R.id.action_save)).perform((click()))

        //Go to note list activity
        pressBack()

        //Check cardview item in recyclerview
        val date = DateTime.now().toString("dd/MM/yyyy")
        onView(withId(R.id.NoteTitle)).check(matches(withText(alias)))
        onView(withId(R.id.CreatedDate)).check(matches(withText("Created date: " + date)))
        onView(withId(R.id.LastEditedDate)).check(matches(withText("Last updated: " + date)))

        //Select note from recyclerview
        onView(withId(R.id.note_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click ()))

        //Authentication dialog
        onView(withId(R.id.password)).perform(typeText(password))
        onView(withId(R.id.OkButton)).perform(click())

        //check note values in NoteFragment
        onView(withId(R.id.notealias)).check(matches(withText(alias)))
        onView(withId(R.id.createdDate)).check(matches(withText(date)))
        onView(withId(R.id.updatedDate)).check(matches(withText(date)))
        onView(withId(R.id.notetext)).check(matches(withText(note)))

        //Go to edit note
        onView(withId(R.id.action_edit)).perform(click())

        //check note values in AddNoteFragment
        onView(withId(R.id.noteName)).check(matches(withText(alias)))
        onView(withId(R.id.lockedCheckBox)).check(matches(isChecked()))
        onView(withId(R.id.noteTextEditing)).check(matches(withText(note)))
        onView(withId(R.id.esen)).check(matches(isChecked()))

    }
}