package com.example.opscprototype

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.util.Calendar
import java.util.Date

class TimesheetPage : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_main_page)

        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
        val progressIcon = findViewById<ImageView>(R.id.timesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheet_profile_button)
        val newTimesheet = findViewById<ImageView>(R.id.timesheet_new_timesheet)
        val layout = findViewById<LinearLayout>(R.id.timesheet_button_layout)
        preloadData()
        var timeSheets = emptyList<String>()

        val timeSheetRef = database.getReference(SharedData.currentUser).child("timeSheets")
        val timeSheetListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (timesheet in snapshot.children) {
                    timeSheets += timesheet.getValue().toString()
                }
                if(snapshot.getValue()!=null) {
                    SharedData.selectedTimeSheet = timeSheets.first()

                    for (timeSheet in timeSheets) {
                        val timesheetName = timeSheet
                        if (timesheetName != null && timesheetName.isNotEmpty()) {
                            val button = Button(this@TimesheetPage)
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
                            button.backgroundTintList =
                                ContextCompat.getColorStateList(this@TimesheetPage, R.color.blue)
                            button.setTextColor(
                                ContextCompat.getColor(
                                    this@TimesheetPage,
                                    R.color.black
                                )
                            )
                            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.toFloat())
                            button.setBackgroundResource(R.drawable.rounded_button)

                            // Add the button to the layout
                            layout.addView(button)

                            button.setOnClickListener {
                                SharedData.selectedTimeSheet = button.text.toString()
                                startActivity(
                                    Intent(
                                        this@TimesheetPage,
                                        TimesheetViewPage::class.java
                                    )
                                )
                            }

                            // Add space after the button
                            val space = Space(this@TimesheetPage)
                            space.layoutParams = LinearLayout.LayoutParams(5, 50)
                            layout.addView(space)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        timeSheetRef.addValueEventListener(timeSheetListener)

        val categoriesRef = database.getReference(SharedData.currentUser).child("categories")
        val categoriesListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<categories>()
                var exists = false
                for (categoriesSnapshot in dataSnapshot.children) {
                    val cat = categoriesSnapshot.child("strName").getValue(String::class.java)
                        ?.let { categories(it) }
                    if (cat != null) {
                        val workLogList = mutableListOf<workLog>()
                        val lstWorkLogDataSnapshot = categoriesSnapshot.child("lstWorkLog")
                        if (lstWorkLogDataSnapshot.key == "lstWorkLog") {
                            val lstWorkLogChildren = lstWorkLogDataSnapshot.children
                            workLogList.clear()
                            for (workLogSnapshot in lstWorkLogChildren) {
                                // Retrieve the properties of workLog from the database
                                val dateWorked = workLogSnapshot.child("dateWorked").getValue(Date::class.java)
                                val amountOfTimeWorkedSeconds = workLogSnapshot.child("amountOfTimeWorked").child("seconds").getValue(Long::class.java)
                                val amountOfTimeWorked = Duration.ofSeconds(amountOfTimeWorkedSeconds ?: 0L)

                                // Create a new workLog object with the retrieved data
                                val workLog = workLog()
                                workLog.dateWorked = dateWorked ?: Calendar.getInstance().time
                                workLog.amountOfTimeWorked = amountOfTimeWorked

                                cat.lstWorkLog += workLog
                            }
                        }

                        val time = categoriesSnapshot.child("hoursWorked").child("seconds").getValue(Long::class.java)
                        if (time != null) {
                            cat.hoursWorked = Duration.ofSeconds(time)
                        }
                        for(category in SharedData.lstCategories){
                            if(category.strName.equals(cat.strName)){
                                exists = true
                            }
                        }
                        if(exists == false || SharedData.lstCategories == emptyList<categories>()){
                            SharedData.lstCategories += cat
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        categoriesRef.addValueEventListener(categoriesListener)

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
        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
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
                "Amazon", emptyList<workLog>()
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
                "Amazon", emptyList<workLog>()
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
                "Amazon", emptyList<workLog>()
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
                "Amazon", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 3)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(3)
            },
            Tasks(
                "Implement database integration",
                "App development",
                "Integrate the mobile app with a backend database using Firebase or SQLite",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                "Amazon", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 3)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Test and debug app functionality",
                "App development",
                "Identify and fix issues in the mobile app by performing testing and debugging",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                "Amazon", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 4)
                date = calendar.time
                dateCreated = date
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
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 4)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(3)
            },
            Tasks(
                "Create project timeline",
                "Planning",
                "Develop a detailed timeline that includes milestones and deadlines for the project",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 4)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(2)
            },
            Tasks(
                "Gather project requirements",
                "Planning",
                "Meet with stakeholders to gather and document the project requirements and scope",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 5)
                date = calendar.time
                dateCreated = date
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
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 5)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Monitor project progress",
                "Project Management",
                "Regularly track and monitor the project's progress to ensure it stays on schedule",
                Date(), Date(),
                "01:00 PM", "03:00 PM",
                1.5, 3.0,
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 5)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(1)
            },
            Tasks(
                "Resolve project issues",
                "Project Management",
                "Address and resolve any issues or conflicts that arise during the project lifecycle",
                Date(), Date(),
                "10:00 AM", "12:00 PM",
                2.0, 4.0,
                "Google", emptyList<workLog>()
            ).apply {
                calendar.set(Calendar.YEAR, 2023)
                calendar.set(Calendar.MONTH, Calendar.JUNE)
                calendar.set(Calendar.DAY_OF_MONTH, 6)
                date = calendar.time
                dateCreated = date
                durTimeWorked = Duration.ofHours(2)
            }
        )

        /* val tasksRef = database.getReference(SharedData.currentUser).child("tasks")
         for(task in tasks){
                 tasksRef.child(task.strTaskName).setValue(task)
             }*/

        var categories = SharedData.lstCategories

        // Preload categories

        if (categories.count() < 2) {
            /*  SharedData.lstCategories += categories("Web design")
              SharedData.lstCategories += categories("App development")
              SharedData.lstCategories += categories("Planning")
              SharedData.lstCategories += categories("Project Management")*/

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
                /*for(cat in SharedData.lstCategories){
                      database.reference.child(SharedData.currentUser).child("categories").child(cat.strName).setValue(cat)
                  }*/
            }
        }
    }
}
