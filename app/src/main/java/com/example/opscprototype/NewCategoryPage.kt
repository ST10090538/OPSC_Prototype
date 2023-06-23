package com.example.opscprototype

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NewCategoryPage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_category_page)

        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
        val progressIcon = findViewById<ImageView>(R.id.newcategory_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newcategory_profile_button)
        val submitButton = findViewById<Button>(R.id.newcategory_submit)

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        submitButton.setOnClickListener {
            val newCat = findViewById<EditText>(R.id.txtNewCatName).text.toString()
            val intent = Intent()
            if(newCat!=""){
                val cat = categories(newCat)
                val ref = database.getReference(SharedData.currentUser)
                ref.child("categories").child(cat.strName).setValue(cat)
            }
            intent.putExtra("newCat", newCat)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}