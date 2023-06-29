package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterPage : AppCompatActivity() {

    private lateinit var editText2: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var editTextTextPassword2: EditText
    private lateinit var registerpage_register_button: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        editText2 = findViewById(R.id.editText2)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2)
        registerpage_register_button = findViewById(R.id.registerpage_register_button)

        registerpage_register_button.setOnClickListener {
            val username = editText2.text.toString().trim()
            val password = editTextTextPassword.text.toString().trim()
            val confpass = editTextTextPassword2.text.toString().trim()

            // Validation for empty username field
            if (TextUtils.isEmpty(username)) {
                editText2.error = "Username is Required!"
                return@setOnClickListener
            }

            // Validation for empty password field
            if (TextUtils.isEmpty(password)) {
                editTextTextPassword.error = "Password is Required!"
                return@setOnClickListener
            }

            // Validation for an eight-character password
            val passwordPattern = Regex("^.{8,}$")  // Regex pattern for 8 or more characters
            if (!password.matches(passwordPattern)) {
                editTextTextPassword.error = "Password must be 8 characters or longer!"
                return@setOnClickListener
            }

            // Validation for empty confirm password field
            if (TextUtils.isEmpty(confpass)) {
                editTextTextPassword2.error = "Confirm Password is Required!"
                return@setOnClickListener
            }

            // Validation if passwords match
            if (password != confpass) {
                editTextTextPassword2.error = "Password does not match!"
                return@setOnClickListener
            }

            // Register user with Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User registration successful
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()


                        val intent = Intent(this, LoginPage::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("password", password)
                        startActivity(intent)
                        finish()
                    } else {
                        // User registration failed
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
