package com.example.realTime_fcm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_book.*
import kotlinx.android.synthetic.main.activity_edit_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class add_Book : AppCompatActivity() {
    lateinit var database: DatabaseReference

    var imageUri: Uri? = null

    var storge: FirebaseStorage? = null

    var myDateBase = "Book"

    var referance: StorageReference? = null

    var counter = 0

    lateinit var path:String

    val topic = "/topics/all"

    var x = "Book"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        database = Firebase.database.reference

        path=String()

        storge= Firebase.storage

        referance=storge!!.reference

        imageView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(gallery, 100)
        }
        butSave.setOnClickListener {

            val book=Book(UUID.randomUUID().toString(),
                editTextName.text.toString(),
                editTextTextAuther.text.toString(),
                editTextTextYear.text.toString(),
                ratingBar.rating.toDouble(),
                editTextTextPrice.text.toString().toInt(),
                path.toString())
            addBook(book)

            PushNotification(
                NotificationData( "Adding Book","has been added Book : ${editTextName.text.toString()}"),
                topic).also {
                sendNotificationForBook(it)
            }
            val i=Intent(this,MainActivity::class.java)

            startActivity(i)
        }

    }

    private fun addBook(book: Book) {

        database.child("Book/${book.id}").setValue(book)

            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {

            imageView.setImageURI(data!!.data)
            imageUri=data.data!!
            upload(imageUri)

        } else {
            Toast.makeText(this, "OMG!!", Toast.LENGTH_LONG).show()
        }
    }

    fun upload(uri: Uri?) {
        referance!!.child("Book/"+ UUID.randomUUID().toString()).putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri ->
                path=uri.toString()
            }
        }.addOnFailureListener {exception ->
            Toast.makeText(this,"what's the wrong", Toast.LENGTH_LONG).show()

        }
    }


    private fun sendNotificationForBook(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}