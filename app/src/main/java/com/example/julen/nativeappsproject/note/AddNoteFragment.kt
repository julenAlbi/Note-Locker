package com.example.julen.nativeappsproject.note

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.databinding.FragmentAddNoteBinding
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.model.Note

class AddNoteFragment : Fragment() {


    private lateinit var noteViewModel: NoteViewModel

    var fragmenCommunication : FragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val acti = context as Activity
        fragmenCommunication = acti as FragmentCommunication
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_save -> {
                saveNote()
                fragmenCommunication?.changeFragment()
                return true
            }
            R.id.action_delete -> {
                noteViewModel.delete()
                activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
                if(!NoteListActivity.twoPane) activity!!.finish()
                return true
            }
        }
        return false
    }

    private fun saveNote(){
        if(noteViewModel.note.value!!.locked) noteViewModel.note.value!!.secret = EncryptionServices(context!!).encrypt(noteViewModel.note.value!!.secret)
        if(arguments?.getString(NoteActivity.NOTE_MODE) == NoteActivity.ADD_NOTE)
            noteViewModel.insert()
        else
            noteViewModel.update()
        if(noteViewModel.note.value!!.locked) noteViewModel.note.value!!.secret = EncryptionServices(context!!).decrypt(noteViewModel.note.value!!.secret)
    }

    /**
     * Creating the view for this fragment.
     * - update UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: FragmentAddNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false)

        var note : Note? = null

        arguments?.let {
            if (it.containsKey(NoteFragment.ARG_NOTE)) {
                // Load the note specified by the fragment
                // arguments.
                note = it.getSerializable(NoteFragment.ARG_NOTE) as Note
                if (note == null) {
                    note = Note(0, "", "")
                }
            }
        }
        //No need of decryptino, because the decryption was done in view mode.
        //if(note!!.locked && arguments?.getString(NoteActivity.NOTE_MODE) == NoteActivity.EDIT_NOTE && note != null) note!!.secret = EncryptionServices(context!!).decrypt(note!!.secret)

        //When geting the viewmodel from the store we not only specify the class but also the note.
        //The note can be null, that's because if it is a fragment change (from [AddNoteFragment] to [NoteFragment]),
        //the viewmodel still exists and contains the note, so we don't have to pass it again.
        //We also have to provide the activity, and not the fragment, as the scope.
        //Otherwise we risk still having two ViewModels (see [android.arch.ViewModelProviders] documentation).
        noteViewModel = ViewModelProviders.of(activity!!, NoteViewModelFactory(note, Application())).get( NoteViewModel::class.java)
        //In twopane mode, viewmodel always is the same, so needs to change the note.
        if(NoteListActivity.twoPane && note != null && arguments?.getString(NoteActivity.NOTE_MODE) == NoteActivity.ADD_NOTE){
            noteViewModel.note.value = note
        }
        binding.noteViewModel = noteViewModel
        binding.setLifecycleOwner(activity)

        noteViewModel.toastMessage.observe(this, Observer { res ->
            if (res != null) {
                val message = res.format(context)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * If a note is editting, note parameter will be null.
         * If a non existing note is adding, note parameter will not be null.
         *
         * @param note the note that is going to be edited. If note is null, a new note is going to be created.
         * @return A new instance of fragment AddNoteFragment.
         */
        @JvmStatic
        fun newInstance(note : Note? = null) = AddNoteFragment().apply {
            note?.let {
                arguments = Bundle().apply {
                    putSerializable(NoteFragment.ARG_NOTE, note)
                    putString(NoteActivity.NOTE_MODE, NoteActivity.ADD_NOTE)
                }
            }?: run{
                arguments = Bundle().apply {
                    putString(NoteActivity.NOTE_MODE, NoteActivity.EDIT_NOTE)
                }
            }
        }
    }
}
