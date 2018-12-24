package com.example.julen.nativeappsproject.note

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.databinding.FragmentNoteBinding
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.fragment_note.view.*

class NoteFragment : Fragment() {


    private lateinit var noteViewModel: NoteViewModel

    var fragmenCommunication : FragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val acti = context as Activity
        fragmenCommunication = acti as FragmentCommunication
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_edit -> {
                fragmenCommunication?.changeFragment()
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

        //When geting the viewmodel from the store we not only specify the class but also the note.
        //The note can be null, that's because if it is a fragment change (from [AddNoteFragment] to [NoteFragment]),
        //the viewmodel still exists and contains the note, so we don't have to pass it again.
        //We also have to provide the activity, and not the fragment, as the scope.
        //Otherwise we risk still having two ViewModels (see [android.arch.ViewModelProviders] documentation).
        noteViewModel = ViewModelProviders.of(activity!!, NoteViewModelFactory(note, Application() )).get( NoteViewModel::class.java)
        if(NoteListActivity.twoPane && note != null) noteViewModel.note.value = note //In twopane mode, viewmodel always is the same, so needs to change the note.
        binding.noteViewModel = noteViewModel
        binding.setLifecycleOwner(activity)

        return binding.root
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
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
