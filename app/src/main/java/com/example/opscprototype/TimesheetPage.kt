package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TimesheetPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_main_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheet_profile_button)
        val newTimesheet = findViewById<ImageView>(R.id.timesheet_new_timesheet)
        val timesheetInfo = findViewById<Button>(R.id.timesheet_one1)

        timesheetInfo.setOnClickListener {
            startActivity(Intent(this, TimesheetViewPage::class.java))
        }

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        newTimesheet.setOnClickListener {
            startActivity(Intent(this, NewTimesheetPage::class.java))
        }
    }
}