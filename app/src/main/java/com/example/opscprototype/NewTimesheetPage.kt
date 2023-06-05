package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class NewTimesheetPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_timesheet_page)
        BackButton_NewTimesheetPage()

        val progressIcon = findViewById<ImageView>(R.id.newtimesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newtimesheet_profile_button)
        val submitButton = findViewById<Button>(R.id.newtimesheet_submit)

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        submitButton.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
    private fun BackButton_NewTimesheetPage() {

        val click = findViewById<View>(R.id.BackButton_NewTimesheetPage)
        click.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}