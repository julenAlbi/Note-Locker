package com.example.julen.nativeappsproject.note

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.databinding.FragmentNoteBinding
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.encription.KeyStoreManager
import com.example.julen.nativeappsproject.model.Note

/**
 * This [Fragment] is used to display the content of a note.
 *
 * @property noteViewModel i the viewmodel that is used by this fragment
 * @property fragmenCommunication is the interface whereby this fragment communicates with its activity.
 */
class NoteFragment : Fragment() {


    private lateinit var noteViewModel: NoteViewModel

    var fragmenCommunication : FragmentCommunication? = null

    /**
     * When the fragment is created, with [setHasOptionsMenu] it is possible to use the toolbar menu.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * This function gives a value to [fragmenCommunication]
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val acti = context as Activity
        fragmenCommunication = acti as FragmentCommunication
    }

    /**
     * Performs an action when menu [item] is selected. In [NoteFragment] case, the user maybe wants
     * to edit the note or to delete the note.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_edit -> {
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

    /**
     * Creating the view for this fragment.
     * - update UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false)

        var note : Note? = null

        arguments?.let {
            if (it.containsKey(ARG_NOTE)) {
                // Load the note specified by the fragment
                // arguments.
                note = it.getSerializable(ARG_NOTE) as Note
            }
        }

        //Decrypt note
        note?.let { if(note!!.locked) note!!.secret = EncryptionServices(context!!).decrypt(note!!.secret) }

        //When geting the viewmodel from the store we not only specify the class but also the note.
        //The note can be null, that's because if it is a fragment change (from [AddNoteFragment] to [NoteFragment]),
        //the viewmodel still exists and contains the note, so we don't have to pass it again.
        //We also have to provide the activity, and not the fragment, as the scope.
        //Otherwise we risk still having two ViewModels (see [android.arch.ViewModelProviders] documentation).
        noteViewModel = ViewModelProviders.of(activity!!, NoteViewModelFactory(note, Application() )).get( NoteViewModel::class.java)
        //In twopane mode, viewmodel always is the same, so needs to change the note.
        if(NoteListActivity.twoPane && note != null){
            noteViewModel.note.value = note
        }
        binding.noteViewModel = noteViewModel
        binding.setLifecycleOwner(activity)

        return binding.root
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * In twopanemode, the node will not never be null, in normal mode, when the activity is crearted will have value, otherwise it will be null.
         *
         * @param note the note that is going to display.
         * @return A new instance of fragment AddNoteFragment.
         */
        const val ARG_NOTE = "item_id"

        @JvmStatic
        fun newInstance(note : Note? = null) = NoteFragment().apply {
            note?.let {
                arguments = Bundle().apply {
                    putSerializable(ARG_NOTE, note)
                }
            }
        }
    }
}
