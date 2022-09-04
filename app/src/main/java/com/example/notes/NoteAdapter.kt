package com.example.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context, noteList: ArrayList<Note>) :
    ArrayAdapter<Note>(context, 0, noteList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.note_row_layout, parent, false)

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)

        val note = getItem(position)
        txtTitle.text = note!!.title
        txtDate.text = note.timestamp.toString()

        return view
    }
}