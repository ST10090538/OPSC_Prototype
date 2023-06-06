package com.example.opscprototype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
            val timesheetName = findViewById<EditText>(R.id.newtimesheet_timesheet_name).text.toString()
            saveTimesheetName(timesheetName)
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
    private fun BackButton_NewTimesheetPage() {

        val click = findViewById<View>(R.id.BackButton_NewTimesheetPage)
        click.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }


    }
    private fun saveTimesheetName(timesheetName: String) {
        val sharedPreferences = getSharedPreferences("Timesheets", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val timesheetCount = sharedPreferences.getInt("TimesheetCount", 0) + 1
        editor.putString("Timesheet$timesheetCount", timesheetName)
        editor.putInt("TimesheetCount", timesheetCount)
        editor.apply()
    }
}