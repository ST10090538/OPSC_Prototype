package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val login = findViewById<Button>(R.id.loginpage_login_button)
        val register = findViewById<Button>(R.id.registerpage_register_button)

        login.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        register.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
    }
}