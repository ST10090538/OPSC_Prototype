package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomePage()
    }

    private fun welcomePage() {
        setContentView(R.layout.welcome_page)

        val click = findViewById<View>(R.id.welcomePage)
        click.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }
    }
}

