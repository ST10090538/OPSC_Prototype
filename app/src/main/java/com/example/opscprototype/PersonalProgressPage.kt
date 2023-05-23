package com.example.opscprototype

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PersonalProgressPage : AppCompatActivity() {
    lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_personalprogress_page)

        //------------


        pieChart = findViewById(R.id.pie_chart)

        val entries = listOf(
            PieEntry(50f, "Web Design"),
            PieEntry(30f, "App Development"),

        )

        val dataSet = PieDataSet(entries, "Pie Chart")
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()

    //--------------

        val progressIcon = findViewById<ImageView>(R.id.personalprogress_progress_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }


    }
}