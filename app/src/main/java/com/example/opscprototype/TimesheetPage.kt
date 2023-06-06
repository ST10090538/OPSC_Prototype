package com.example.opscprototype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TimesheetPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_main_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheet_profile_button)
        val newTimesheet = findViewById<ImageView>(R.id.timesheet_new_timesheet)
        val layout = findViewById<LinearLayout>(R.id.timesheet_button_layout)

        val sharedPreferences = getSharedPreferences("Timesheets", Context.MODE_PRIVATE)
        val timesheetCount = sharedPreferences.getInt("TimesheetCount", 0)

        for (i in 1..timesheetCount) {
            val timesheetName = sharedPreferences.getString("Timesheet$i", "")
            if (timesheetName != null && timesheetName.isNotEmpty()) {
                val button = Button(this)
                button.text = timesheetName

                // Set button attributes
                button.layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.new_button_width),
                    resources.getDimensionPixelSize(R.dimen.new_button_height)
                )
                button.setPadding(
                    resources.getDimensionPixelSize(R.dimen.button_padding_start),
                    0,
                    resources.getDimensionPixelSize(R.dimen.button_padding_end),
                    0
                )
                button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
                button.setTextColor(ContextCompat.getColor(this, R.color.black))
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.toFloat())
                button.setBackgroundResource(R.drawable.rounded_button)

                // Add the button to the layout
                layout.addView(button)

                // Set click listener for the button
                button.setOnClickListener {
                    val intent = Intent(this, TimesheetViewPage::class.java)
                    intent.putExtra("timesheetName", timesheetName)
                    startActivity(intent)
                }

                // Create a new button for tasks
                val taskButton = Button(this)
                taskButton.text = "New Task"

                // Set task button attributes
                taskButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                taskButton.setPadding(
                    resources.getDimensionPixelSize(R.dimen.button_padding_start),
                    0,
                    resources.getDimensionPixelSize(R.dimen.button_padding_end),
                    0
                )
                taskButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue)
                taskButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                taskButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
                taskButton.setBackgroundResource(R.drawable.rounded_button)

                // Add the task button to the layout
                layout.addView(taskButton)

                // Set click listener for the task button
                taskButton.setOnClickListener {
                    val intent = Intent(this, TimesheetViewPage::class.java)
                    intent.putExtra("timesheetName", timesheetName)
                    startActivity(intent)
                }
            }
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
