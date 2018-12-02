package com.example.julen.nativeappsproject

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.fragment_note.view.*

class NoteFragment : Fragment() {

    /**
     * The note this fragment is representing
     */
    private lateinit var note: Note

    var fragmenCommunication : FragmentCommunication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        arguments!!.let {
            if (it.containsKey(ARG_NOTE)) {
                // Load the note specified by the fragment
                // arguments.
                note = it.getSerializable(ARG_NOTE) as Note
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
            R.id.action_edit -> {
                Log.d("DEBUGGIN","Save_action item selected.")
                fragmenCommunication?.changeFragment(note)
                return true
            }
        }
        Log.d("DEBUGGIN","Bad itemselected.")
        return false
    }

    /**
     * Creating the view for this fragment.
     * - update UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_note, container, false)

        //activity?.toolbar_note?.title = note.alias

        // Show the note content
        // The let construct calls the specified function block with this value as its argument
        // and returns its result.
        note.let {
            rootView.notealias.text = it.alias
            rootView.createdDate.text = it.createDate.toString("dd/mm/yyyy")
            rootView.updatedDate.text = it.updateDate.toString("dd/mm/yyyy")
            rootView.notetext.append(it.secret.toString())
        }
        return rootView
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
        fun newInstance(note : Note) = NoteFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_NOTE, note)
                    }
                }
    }
}
