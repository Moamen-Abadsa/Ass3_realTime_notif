package com.example.realTime_fcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var arrayBook:ArrayList<Book>

     var booksAdapter: booksAdapter?=null

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     arrayBook= arrayListOf()

        database = Firebase.database.reference

        val userListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (doc in dataSnapshot.children) {

                    arrayBook.add(Book(doc.child("id").value.toString(),
                        doc.child("name").value.toString(),
                        doc.child("auther").value.toString(),
                        doc.child("date").value.toString()
                        ,doc.child("rating").value.toString().toDouble(),
                        doc.child("price").value.toString().toInt(),
                        doc.child("image").value.toString()))
                }
                booksAdapter=booksAdapter(this@MainActivity,arrayBook)
                rv.adapter=booksAdapter
                rv.layoutManager=LinearLayoutManager(this@MainActivity)

            }


            override fun onCancelled(databaseError: DatabaseError) {

                //Log.w(TAG, "loadUser:onCancelled", databaseError.toException())
            }

        }
        database.child("books").addValueEventListener(userListener)






        floatingActionButton.setOnClickListener {
            val i=Intent(this,add_Book::class.java)
            startActivity(i)
        }


    }
}