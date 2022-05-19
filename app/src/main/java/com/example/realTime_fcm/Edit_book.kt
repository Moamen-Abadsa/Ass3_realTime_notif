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
import kotlin.collections.HashMap

class Edit_book : AppCompatActivity() {
    lateinit var database: DatabaseReference
    var imageUri: Uri? = null
    var storge: FirebaseStorage? = null
    var referance: StorageReference? = null
    lateinit var name: String
    lateinit var image:String
    lateinit var book:Book
    val topic = "/topics/all"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        database = Firebase.database.reference
        storge = Firebase.storage
        referance = storge!!.reference
        book = intent.getSerializableExtra("book") as Book
        Edit_name.text.append(book.name)
        Edit_auther.text.append(book.auther)
        edit_year.text.append(book.date)
        ratingBar2.rating= book.rate.toDouble().toFloat()
        edit_price.text.append(book.price.toString())
        image = book.image.toString()
        imageView2.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 400)

        }
        edit.setOnClickListener {
//            val book=Book(Edit_name.text.toString(),Edit_auther.text.toString(),edit_year.text.toString(),ratingBar2.rating.toDouble(),edit_price.text.toString().toInt(),path)
//            updateB(book)
            val book = hashMapOf(
                "name" to Edit_name.text.toString(),
                "auther" to Edit_auther.text.toString(),
                "date" to edit_year.text.toString(),
                "rate" to ratingBar2.rating.toDouble(),
                "price" to edit_price.text.toString().toInt(),
                "image" to image
            ) as Map<String, Any>

            updateBoo(book)

            val i =Intent(this
            ,MainActivity::class.java)
            startActivity(i)
            sendNotification(PushNotification(
                NotificationData(
                "Ebitbook","EDITbook${Edit_name.text.toString()}"
            ),
                topic).also {
                sendNotification(it)
            }
            )
        }
        delete.setOnClickListener {
            database.child("Book").child(book.id).removeValue()
                .addOnSuccessListener {}
                .addOnFailureListener {}
            val i =Intent(this
                ,MainActivity::class.java)
            startActivity(i)

            sendNotification(PushNotification(
                NotificationData(
                    "deletebook","deletebook"
                ),
                topic).also {
                sendNotification(it)
            }
            )
        }
    }

    private fun updateBoo(bookk: Map<String, Any>) {
        database.child("Book").child(book.id).updateChildren(bookk)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 400) {

            imageView2.setImageURI(data!!.data)
            imageUri = data.data!!
            upload(imageUri)

        } else {
            Toast.makeText(this, "f", Toast.LENGTH_LONG).show()
        }
    }

    fun upload(uri: Uri?) {
        referance!!.child("BookImages/" + UUID.randomUUID().toString()).putFile(uri!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    image = uri.toString()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "filed", Toast.LENGTH_LONG).show()
            }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
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