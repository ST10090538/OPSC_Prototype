package com.example.opscprototype

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.transition.Fade
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.Constraint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomePage()
    }

    private fun welcomePage() {
        setContentView(R.layout.welcome_page)

        val click = findViewById<View>(R.id.welcomePage)

        click.setOnClickListener(){
            loginPage()
        }
    }

    fun loginPage(){
    setContentView(R.layout.login_page)

        val login = findViewById<Button>(R.id.loginpage_login_button)



        val register = findViewById<Button>(R.id.registerpage_register_button)


        login.setOnClickListener(){
            timesheetPage()
        }

        register.setOnClickListener(){
            registerPage()
        }


    }

    fun registerPage(){
        setContentView(R.layout.register_page)
        lateinit var provinceSpinner: Spinner
        provinceSpinner = findViewById(R.id.provinceSpinner)
        val provinces = arrayOf(
            "Eastern Cape",
            "Free State",
            "Gauteng",
            "KwaZulu-Natal",
            "Limpopo",
            "Mpumalanga",
            "North West",
            "Northern Cape",
            "Western Cape"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        provinceSpinner.adapter = adapter


        val register = findViewById<Button>(R.id.registerpage_register_button)
        register.setOnClickListener(){
            loginPage()
        }
    }

    fun timesheetPage() {
        setContentView(R.layout.timesheet_main_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheet_profile_button)
        val newTimesheet = findViewById<ImageView>(R.id.timesheet_new_timesheet)
        val timesheetInfo = findViewById<Button>(R.id.timesheet_one1)

        timesheetInfo.setOnClickListener(){
            timesheetViewPage()
        }
        progressIcon.setOnClickListener {
            progressPage()
        }

        profileIcon.setOnClickListener {
            profilePage()
        }

        newTimesheet.setOnClickListener {
            newTimesheetPage()
       }


    }
    private fun progressPage() {
        setContentView(R.layout.progress_main_page)

        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val personalProgressIcon = findViewById<Button>(R.id.progress_personalprogress_button)
        //val hoursworkedIcon = findViewById<TextView>(R.id.hoursworked_button)

        timesheetIcon.setOnClickListener {
            timesheetPage()
        }
        personalProgressIcon.setOnClickListener {
            personalProgressPage()
        }
        profileIcon.setOnClickListener {
            profilePage()
        }
        // hoursworkedIcon.setOnClickListener {
        //     hoursworked()
        // }
    }

    private fun profilePage() {
        setContentView(R.layout.profile_page)

        val progressIcon = findViewById<ImageView>(R.id.profile_progress_button)
        val timesheetIcon = findViewById<ImageView>(R.id.profile_timesheet_button)
        progressIcon.setOnClickListener {
            progressPage()
        }
        timesheetIcon.setOnClickListener {
            timesheetPage()
        }
    }

    private fun hoursWorkedPage() {
        setContentView(R.layout.progress_hoursworked_page)

        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)

        profileIcon.setOnClickListener {
            profilePage()
        }
        timesheetIcon.setOnClickListener {
            timesheetPage()
        }

    }


    private fun newTimesheetPage() {
        setContentView(R.layout.new_timesheet_page)

        val progressicon = findViewById<ImageView>(R.id.newtimesheet_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newtimesheet_profile_button)
        val submit = findViewById<Button>(R.id.newtimesheet_submit)

        profileIcon.setOnClickListener {
            profilePage()
        }
        progressicon.setOnClickListener {
            progressPage()
        }
        submit.setOnClickListener {
            timesheetPage()
        }
    }
    private fun newCategoryPage() {
        setContentView(R.layout.new_category_page)
        val progressicon = findViewById<ImageView>(R.id.newcategory_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newcategory_profile_button)
        val submit = findViewById<Button>(R.id.newcategory_submit)

        profileIcon.setOnClickListener {
            profilePage()
        }
        progressicon.setOnClickListener {
            progressPage()
        }
        submit.setOnClickListener {
            newTaskPage()
        }
    }

    private fun timesheetViewPage(){
        setContentView(R.layout.timesheet_view_page)

        val progressicon = findViewById<ImageView>(R.id.timesheetview_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheetview_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.timesheetview_timesheet_button)
        val newTask = findViewById<ImageView>(R.id.timesheetview_newtask_button)


        profileIcon.setOnClickListener {
            profilePage()
        }
        progressicon.setOnClickListener {
            progressPage()
        }
        timesheetIcon.setOnClickListener {
            timesheetPage()
        }
        newTask.setOnClickListener {
            newTaskPage()
        }
    }

    private fun personalProgressPage(){
        setContentView(R.layout.progress_personalprogress_page)

        val progressicon = findViewById<ImageView>(R.id.personalprogress_progress_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)


        profileIcon.setOnClickListener {
            profilePage()
        }
        progressicon.setOnClickListener {
            progressPage()
        }
        timesheetIcon.setOnClickListener {
            timesheetPage()
        }

    }

    private fun newTaskPage(){
        setContentView(R.layout.new_task_page)
        val progressicon = findViewById<ImageView>(R.id.newtask_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newtask_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.newtask_timesheet_button)
        val newCategory = findViewById<Button>(R.id.newtask_new_category_button)
        val submit = findViewById<Button>(R.id.newtask_submit_button)

        lateinit var categorySpinner: Spinner
        categorySpinner = findViewById(R.id.categorySpinner)
        val categories = arrayOf(
           "Web Design",
            "App Development"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        submit.setOnClickListener {
            timesheetViewPage()
        }
        newCategory.setOnClickListener {
            newCategoryPage()
        }
        profileIcon.setOnClickListener {
            profilePage()
        }
        progressicon.setOnClickListener {
            progressPage()
        }
        timesheetIcon.setOnClickListener {
            timesheetPage()
        }
    }
    }
