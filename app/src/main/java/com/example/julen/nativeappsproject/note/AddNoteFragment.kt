package com.example.julen.nativeappsproject.note

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import org.joda.time.DateTime

class AddNoteFragment : Fragment() {

    /**
     * The note this fragment is representing
     */
    private var note: Note? = null

    var fragmenCommunication : FragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        arguments?.let {
            if (it.containsKey(NoteFragment.ARG_NOTE)) {
                // Load the note specified by the fragment
                // arguments.
                note = it.getSerializable(NoteFragment.ARG_NOTE) as Note
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val acti = context as Activity
        fragmenCommunication = acti as FragmentCommunication
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_save -> {
                Log.d("DEBUGGIN","Save_action item selected.")
                fragmenCommunication?.changeFragment(saveNote())
                return true
            }
        }
        Log.d("DEBUGGIN","Bad itemselected.")
        return false
    }

    private fun saveNote(): Note {

        return (Note(noteName.text.toString(),noteTextEditing.text.toString(), DateTime.now(), DateTime.now(),lockedCheckBox.isChecked))

    }

    /**
     * Creating the view for this fragment.
     * - update UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_note, container, false)

        //activity?.toolbar_note?.title = note.alias

        // Show the note content
        // The let construct calls the specified function block with this value as its argument
        // and returns its result.
        note?.let {
            rootView.noteName.append(it.alias)
            rootView.lockedCheckBox.isChecked = it.locked
            rootView.noteTextEditing.append(it.secret.toString())
        }
        return rootView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param note the note that is going to be edited. If note is null, a new note is going to be created.
         * @return A new instance of fragment AddNoteFragment.
        */
        @JvmStatic
        fun newInstance(note : Note? = null) = AddNoteFragment().apply {
            note?.let {
                arguments = Bundle().apply {
                    putSerializable(NoteFragment.ARG_NOTE, note)
                }
            }
        }
    }
}
