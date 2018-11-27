package com.example.julen.nativeappsproject

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.note_list_content.view.*
import kotlinx.android.synthetic.main.note_list_content.*

class NoteListAdapter(private val parentActivity: NoteListActivity,
                      private val notes: List<Note>,
                      private val twoPane: Boolean) :
        RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private var showNoteFragment: ((note: Note) -> Unit)? = null
    private var showNoteActivity: ((note: Note) -> Unit)? = null

    init {
        onClickListener = View.OnClickListener { v ->

            val item = v.tag as Note
            if (twoPane) {
                showNoteFragment?.invoke(item)
            } else {
                showNoteActivity?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.name.text = note.alias
        holder.lastEditedDate.text = note.updateDate.toString()
        //is locked?
        if (true) {
            holder.locked.visibility = View.VISIBLE
            holder.unlocked.visibility = View.INVISIBLE
        }

        with(holder.itemView) {
            tag = note // Save the comic represented by this view
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = notes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.NoteTitle
        val lastEditedDate: TextView = view.LastEditedDate
        val locked: TextView = view.lockedIcon
        val unlocked: TextView = view.unlockedIcon
    }
}