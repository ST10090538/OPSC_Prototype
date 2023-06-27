package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var editText2: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var registerpage_register_button: Button
    private lateinit var firebaseAuth: FirebaseAuth

    private var registeredUsername: String? = null
    private var registeredPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        editText2 = findViewById(R.id.editText2)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        findViewById<Button>(R.id.loginpage_login_button)
        registerpage_register_button = findViewById(R.id.registerpage_register_button)

        registerpage_register_button.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }

        findViewById<Button>(R.id.loginpage_login_button).setOnClickListener() {
            val username = editText2.text.toString().trim()
            val password = editTextTextPassword.text.toString().trim()

            if (TextUtils.isEmpty(username)) {
                editText2.error = "Username is Required!"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)) {
                editTextTextPassword.error = "Password is Required!"
                return@setOnClickListener
            }

            // Authenticate user with Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User login successful
                        Toast.makeText(this, "Logged In Successfully!", Toast.LENGTH_SHORT).show()

                        // Set the current user
                        SharedData.currentUser = firebaseAuth.currentUser?.uid.toString()
                        startActivity(Intent(this, TimesheetPage::class.java))
                        finish()
                    } else {
                        // User login failed
                        Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
