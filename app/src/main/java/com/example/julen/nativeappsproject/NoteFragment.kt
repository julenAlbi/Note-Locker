package com.example.julen.nativeappsproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.fragment_note.view.*


class NoteFragment : Fragment() {

    /**
     * The note this fragment is representing
     */
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments!!.let {
            if (it.containsKey(ARG_NOTE)) {
                // Load the note specified by the fragment
                // arguments.
                note = it.getSerializable(ARG_NOTE) as Note
            }
        }

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
         * The fragment argument representing the item ID that this fragment
         * represents.
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
