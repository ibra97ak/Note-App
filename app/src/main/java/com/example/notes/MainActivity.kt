package com.example.notes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    var mRef: DatabaseReference? = null
    var mNoteList: ArrayList<Note>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingAddNote = findViewById<FloatingActionButton>(R.id.floatingAddNote)
        val listView = findViewById<ListView>(R.id.listView)


        val firebaseDatabase = FirebaseDatabase.getInstance()
        mRef = firebaseDatabase.getReference("Notes")

        mNoteList = ArrayList()



        floatingAddNote.setOnClickListener {
            showDialogAddNote()
        }




        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                var myNote: Note = mNoteList?.get(position)!!

                var alertBuilder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.edit_note, null)
                alertBuilder.setView(view)
                val alertDialog = alertBuilder.create()
                alertDialog.show()

                val btnDelet = view.findViewById<Button>(R.id.btnDelet)
                val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
                val edtNoteTitle = view.findViewById<EditText>(R.id.edtNoteTitle)
                val edtNoteNote = view.findViewById<EditText>(R.id.edtNoteNote)




                edtNoteTitle.setText(myNote.title)
                edtNoteNote.setText(myNote.note)

                btnUpdate.setOnClickListener {


                    var afterUpdate = Note(
                        myNote.id!!,
                        edtNoteTitle.text.toString(),
                        edtNoteNote.text.toString(),
                        getCurrentDate()
                    )


                    mRef?.child(myNote.id.toString())?.setValue(afterUpdate)

                    alertDialog.dismiss()
                }

                btnDelet.setOnClickListener {
                    mRef?.child(myNote.id.toString())?.removeValue()
                    alertDialog.dismiss()
                }


                false
            }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                var myNote = mNoteList?.get(position)!!

                var intent = Intent(this, NoteActivity::class.java)
                intent.putExtra("intentTitle", myNote.title)
                intent.putExtra("intentNote", myNote.note)
                startActivity(intent)

            }


    }


    override fun onStart() {
        super.onStart()

        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mNoteList?.clear()
                for (n in snapshot.children) {
                    val note = n.getValue(Note::class.java)
                    mNoteList!!.add(0, note!!)
                }
                val noteAdapter = NoteAdapter(applicationContext, mNoteList!!)
                val listView = findViewById<ListView>(R.id.listView)
                listView.adapter = noteAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun showDialogAddNote() {

        //Show Dialog

        var alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_note, null)
        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()


        //Save Data in Firebase

        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
        val edtNote = view.findViewById<EditText>(R.id.edtNote)

        btnAdd.setOnClickListener {
            val title = edtTitle.text.toString()
            val note = edtNote.text.toString()
            if (title.isNotEmpty() && note.isNotEmpty()) {
                var id = mRef!!.push().key

                var myNote = Note(id.toString(), title, note, getCurrentDate())
                mRef!!.child(id!!).setValue(myNote)
                alertDialog.dismiss()

            } else if (title.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Title", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please Enter Your Note", Toast.LENGTH_LONG).show()

            }
        }


    }

    private fun getCurrentDate(): String {
        val calender = Calendar.getInstance()
        val mdformat = SimpleDateFormat("EEEE hh:mm a ")
        val strDate = mdformat.format(calender.time)
        return strDate
    }


}