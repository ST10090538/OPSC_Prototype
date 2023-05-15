package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProgressPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_main_page)

        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val personalProgressIcon = findViewById<Button>(R.id.progress_personalprogress_button)

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        personalProgressIcon.setOnClickListener {
            startActivity(Intent(this, PersonalProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }
    }
}
