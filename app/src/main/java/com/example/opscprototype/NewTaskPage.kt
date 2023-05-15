package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class NewTaskPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_task_page)

        val progressIcon = findViewById<ImageView>(R.id.newtask_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newtask_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.newtask_timesheet_button)
        val newCategoryButton = findViewById<Button>(R.id.newtask_new_category_button)
        val submitButton = findViewById<Button>(R.id.newtask_submit_button)

        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val categories = arrayOf("Web Design", "App Development")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        submitButton.setOnClickListener {
            startActivity(Intent(this, TimesheetViewPage::class.java))
        }

        newCategoryButton.setOnClickListener {
            startActivity(Intent(this, NewCategoryPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}