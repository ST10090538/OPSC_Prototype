package com.example.opscprototype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
        if(!timesheetName.equals("")){
            val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
            val timeSheetRef = database.getReference(SharedData.currentUser).child("timeSheets")
            timeSheetRef.child("$timesheetName").setValue("$timesheetName")
        }
    }
}