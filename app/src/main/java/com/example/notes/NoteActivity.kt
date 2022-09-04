package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val txtEditTitle = findViewById<TextView>(R.id.txtEditTitle)
        val txtEditNote = findViewById<TextView>(R.id.txtEditNote)

        txtEditTitle.text = intent.extras?.getString("intentTitle")
        txtEditNote.text = intent.extras?.getString("intentNote")

    }
}