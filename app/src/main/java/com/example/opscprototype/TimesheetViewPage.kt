package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.opscprototype.SharedData

class TimesheetViewPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_view_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheetview_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheetview_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.timesheetview_timesheet_button)
        val newTask = findViewById<ImageView>(R.id.timesheetview_newtask_button)


        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        newTask.setOnClickListener {
            val intent = Intent(this, NewTaskPage::class.java)
            intent.putExtra("newCat", "")
            startActivity(intent)
        }
    }
}