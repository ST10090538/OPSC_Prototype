package com.example.opscprototype

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.Duration
import java.util.Calendar
import java.util.Date

class TimesheetPage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_main_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheet_profile_button)
        val newTimesheet = findViewById<ImageView>(R.id.timesheet_new_timesheet)
        val layout = findViewById<LinearLayout>(R.id.timesheet_button_layout)
        preloadData()

        if(SharedData.lstTimesheets.isEmpty()){
            SharedData.lstTimesheets += "Amazon"
            SharedData.lstTimesheets += "Google"
        }

        for (timeSheet in SharedData.lstTimesheets) {
            val timesheetName = timeSheet
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

                button.setOnClickListener(){
                    SharedData.selectedTimeSheet = button.text.toString()
                    startActivity(Intent(this, TimesheetViewPage::class.java))
                }

                // Add space after the button
                val space = Space(this)
                space.layoutParams = LinearLayout.LayoutParams(5, 50)
                layout.addView(space)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun preloadData() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2023)
        calendar.set(Calendar.MONTH, Calendar.JUNE)
        calendar.set(Calendar.DAY_OF_MONTH, 6)
        var date = calendar.time
        val tasks = listOf(
            // Web design tasks
            Tasks(
                "Design landing page",
                "Web design",
                "Create a visually appealing and user-friendly landing page for a website",
                Date(), Date(),
                "09:00 AM", "11:00 AM",
                2.5, 5.0,
                null, "Amazon"
            ).apply {
                var workLog = workLog()
                workLog.amountOfTimeWorked = Duration.ofHours(2)
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                date = calendar.time
                workLog.dateWorked = date
                dateCreated = date
                durTimeWorked = Duration.ofHours(3)
                lstWorkLog += workLog
            },
            Tasks(
                "Develop login functionality",
                "Web design",
                "Implement login functionality using HTML, CSS, and JavaScript",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                null, "Amazon"
            ).apply {
                var workLog = workLog()
                workLog.amountOfTimeWorked = Duration.ofHours(2)
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 2)
                date = calendar.time
                workLog.dateWorked = date
                dateCreated = date
                durTimeWorked = Duration.ofHours(2)
                lstWorkLog += workLog
            },
            Tasks(
                "Optimize website performance",
                "Web design",
                "Improve website performance by optimizing code, images, and caching",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                null, "Amazon"
            ).apply {
                var workLog = workLog()
                workLog.amountOfTimeWorked = Duration.ofHours(2)
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 2)
                date = calendar.time
                workLog.dateWorked = date
                dateCreated = date
                durTimeWorked = Duration.ofHours(4)
                lstWorkLog += workLog
            },

            // App development tasks
            Tasks(
                "Create mobile app UI",
                "App development",
                "Design the user interface for a mobile application using Sketch or Figma",
                Date(), Date(),
                "09:00 AM", "11:00 AM",
                2.5, 5.0,
                null, "Amazon"
            ).apply {
                durTimeWorked = Duration.ofHours(3)
            },
            Tasks(
                "Implement database integration",
                "App development",
                "Integrate the mobile app with a backend database using Firebase or SQLite",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                null, "Amazon"
            ).apply {
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Test and debug app functionality",
                "App development",
                "Identify and fix issues in the mobile app by performing testing and debugging",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                null, "Amazon"
            ).apply {
                durTimeWorked = Duration.ofHours(2)
            },

            // Planning tasks
            Tasks(
                "Define project goals",
                "Planning",
                "Identify and outline the goals, objectives, and deliverables for the project",
                Date(), Date(),
                "09:00 AM", "11:00 AM",
                2.5, 5.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(3)
            },
            Tasks(
                "Create project timeline",
                "Planning",
                "Develop a detailed timeline that includes milestones and deadlines for the project",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(2)
            },
            Tasks(
                "Gather project requirements",
                "Planning",
                "Meet with stakeholders to gather and document the project requirements and scope",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(5)
            },

            // Project Management tasks
            Tasks(
                "Assign tasks to team members",
                "Project Management",
                "Allocate specific tasks to team members based on their skills and availability",
                Date(), Date(),
                "09:00 AM", "11:00 AM",
                2.5, 5.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Monitor project progress",
                "Project Management",
                "Regularly track and monitor the project's progress to ensure it stays on schedule",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Resolve project issues",
                "Project Management",
                "Address and resolve any issues or conflicts that arise during the project lifecycle",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                null, "Google"
            ).apply {
                durTimeWorked = Duration.ofHours(2)
            }
        )

        SharedData.lstTasks = tasks
        var categories = SharedData.lstCategories

        // Preload categories

        if (categories.count() < 2) {
            SharedData.lstCategories += categories("Web design")
            SharedData.lstCategories += categories("App development")
            SharedData.lstCategories += categories("Planning")
            SharedData.lstCategories += categories("Project Management")

            for (cat in SharedData.lstCategories) {
                if (cat.strName.equals("Web design")) {
                    cat.hoursWorked = Duration.ofHours(9)
                    var workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(2)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(2)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 2)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(5)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 2)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                }
                if (cat.strName.equals("App development")) {
                    cat.hoursWorked = Duration.ofHours(6)
                    var workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(2)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 3)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(4)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 4)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                }
                if (cat.strName.equals("Planning")) {
                    cat.hoursWorked = Duration.ofHours(10)
                    var workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(4)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 3)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(2)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 4)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(4)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 4)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                }
                if (cat.strName.equals("Project Management")) {
                    cat.hoursWorked = Duration.ofHours(4)
                    var workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(1)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 5)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                    workLog = workLog()
                    workLog.amountOfTimeWorked = Duration.ofHours(3)
                    calendar.set(Calendar.YEAR, 2023)
                    calendar.set(Calendar.MONTH, Calendar.JUNE)
                    calendar.set(Calendar.DAY_OF_MONTH, 6)
                    date = calendar.time
                    workLog.dateWorked = date
                    cat.lstWorkLog += workLog
                }
            }
        }
    }
}
