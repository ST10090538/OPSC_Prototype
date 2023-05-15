package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PersonalProgressPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_personalprogress_page)

        val progressIcon = findViewById<ImageView>(R.id.personalprogress_progress_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}