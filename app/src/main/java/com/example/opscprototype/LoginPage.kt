package com.example.opscprototype

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginPage : AppCompatActivity() {

    private lateinit var editText2: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var registerpage_register_button: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")

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
                        val achievementsRef = database.getReference(SharedData.currentUser)
                        val achievementsListener = object: ValueEventListener{
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if(dataSnapshot.hasChild("Achievements")){
                                    SharedData.achievements.firstLoginAchievement =
                                        dataSnapshot.child("Achievements").child("firstLoginAchievement").value as Boolean
                                    SharedData.achievements.profilePicAchievement =
                                        dataSnapshot.child("Achievements").child("profilePicAchievement").value as Boolean
                                    SharedData.achievements.firstTaskAchievement =
                                        dataSnapshot.child("Achievements").child("firstTaskAchievement").value as Boolean

                                    if(SharedData.achievements.firstLoginAchievement == false){
                                        achievementsRef.child("Achievements").child("firstLoginAchievement").setValue(true)
                                        SharedData.achievements.firstLoginAchievement = true
                                        Toast.makeText(this@LoginPage, "Achievement unlocked!\nFirst LOGIN", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    achievementsRef.child("Achievements").setValue(SharedData.achievements)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        }
                        achievementsRef.addValueEventListener(achievementsListener)

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
