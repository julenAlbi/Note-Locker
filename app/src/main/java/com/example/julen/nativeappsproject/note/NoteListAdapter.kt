package com.example.julen.nativeappsproject.note

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.note_list_content.view.*

class NoteListAdapter(private val twoPane: Boolean) :
        RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    var showNoteFragment: ((note: Note) -> Unit)? = null
    var showNoteActivity: ((note: Note) -> Unit)? = null

    private var notes: List<Note>? = null

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

    fun setNotes(newnotes : List<Note>){
        this.notes = newnotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        notes.let{
            val note = notes!![position]
            holder.name.text = note.alias
            holder.lastEditedDate.text = note.updateDate.toString("dd/mm/yyyy")
            holder.createdDate.text = note.createDate.toString("dd/mm/yyyy")
            //is locked?
            if (note.locked) {
                holder.locked.visibility = View.VISIBLE
                holder.unlocked.visibility = View.INVISIBLE
            }else{
                holder.locked.visibility = View.INVISIBLE
                holder.unlocked.visibility = View.VISIBLE
            }

            with(holder.itemView) {
                tag = note // Save the note represented by this view
                setOnClickListener(onClickListener)
            }
        }

    }

    override fun getItemCount() : Int{
        notes?.let {
            return notes!!.size
        }
        return 0
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.NoteTitle
        val lastEditedDate: TextView = view.LastEditedDate
        val createdDate: TextView = view.CreatedDate
        val locked: TextView = view.lockedIcon
        val unlocked: TextView = view.unlockedIcon
    }
}