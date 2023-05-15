package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HoursWorkedPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_hoursworked_page)

        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}