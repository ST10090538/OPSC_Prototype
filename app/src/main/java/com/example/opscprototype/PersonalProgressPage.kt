package com.example.opscprototype


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class PersonalProgressPage : AppCompatActivity() {
    private lateinit var pieChart: PieChart
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_personalprogress_page)
        pieChart = findViewById(R.id.pie_chart)
//---
        val entries: MutableList<PieEntry> = ArrayList()
        val labels: MutableList<String> = ArrayList()

// Add user-generated labels and corresponding values
        labels.add("Web Design")
        labels.add("App Development")
        labels.add("Label 3")
        labels.add("Label 4")

        for (i in labels.indices) {
            val label = labels[i]
            // Here, you can prompt the user for input or obtain the values in any desired way
            val calculatedNumber1 = 50f + 30f // Example calculation
            val calculatedNumber2 = 20f * 2f // Example calculation
            val calculatedNumber3 = 100f / 5f // Example calculation

            entries.add(PieEntry(calculatedNumber1, label))
            entries.add(PieEntry(calculatedNumber2, label))
            entries.add(PieEntry(calculatedNumber3, label))
        }

        val dataSet = PieDataSet(entries, "Personal Progress Pie Chart")
        dataSet.valueTextColor = Color.BLACK // Set the desired text color
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()
        /*
        val labels = listOf("Web Design", "App Development", "Label 3", "Label 4") // Replace with user-generated labels

        val entries: MutableList<PieEntry> = ArrayList()

// Calculate the numbers


        val calculatedNumber1 = 50f + 30f // Example calculation
        val calculatedNumber2 = 20f * 2f // Example calculation
        val calculatedNumber3 = 100f / 5f // Example calculation

// Create PieEntry objects with calculated numbers
        entries.add(PieEntry(calculatedNumber1, "Label 1"))
        entries.add(PieEntry(calculatedNumber2, "Label 2"))
        entries.add(PieEntry(calculatedNumber3, "Label 3"))


        val dataSet = PieDataSet(entries, "Pie Chart")
        dataSet.valueTextColor = Color.BLACK // Set the desired text color
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()
*/
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