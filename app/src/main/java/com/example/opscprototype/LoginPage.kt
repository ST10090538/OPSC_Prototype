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

class LoginPage : AppCompatActivity() {

    companion object {
        const val ACHIEVEMENT_FIRST_LOGIN = "achievement_first_login"
        const val PREFS_FILE_NAME = "achievements_prefs"
        const val PREF_KEY_ACHIEVEMENT_PREFIX = "achievement_"
    }

    private lateinit var editText2: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var registerpage_register_button: Button
    private lateinit var firebaseAuth: FirebaseAuth

    private var registeredUsername: String? = null
    private var registeredPassword: String? = null

    private lateinit var achievementsPrefs: SharedPreferences
    private var isFirstLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        achievementsPrefs = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)

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
        // Check if the "First Login" achievement has already been achieved
        if (isAchievementUnlocked(ACHIEVEMENT_FIRST_LOGIN).not()) {
            showAchievementDialog("First Login")
            setAchievementUnlocked(ACHIEVEMENT_FIRST_LOGIN)
        }
    }
    private fun showAchievementDialog(achievementName: String) {
        val dialogTitle = "Achievement Unlocked"
        val dialogMessage = "Congratulations! You have unlocked the \"$achievementName\" achievement."

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun isAchievementUnlocked(achievementId: String): Boolean {
        return achievementsPrefs.getBoolean(getAchievementPrefKey(achievementId), false)
    }

    private fun setAchievementUnlocked(achievementId: String) {
        val editor = achievementsPrefs.edit()
        editor.putBoolean(getAchievementPrefKey(achievementId), true)
        editor.apply()
    }

    private fun getAchievementPrefKey(achievementId: String): String {
        return PREF_KEY_ACHIEVEMENT_PREFIX + achievementId
    }
}
