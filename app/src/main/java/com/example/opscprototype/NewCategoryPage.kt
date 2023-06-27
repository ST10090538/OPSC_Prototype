package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewCategoryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_category_page)

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
            intent.putExtra("newCat", newCat)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}