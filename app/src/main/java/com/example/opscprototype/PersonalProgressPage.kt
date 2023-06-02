package com.example.opscprototype


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Duration


class PersonalProgressPage : AppCompatActivity() {
    private lateinit var pieChart: PieChart

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_personalprogress_page)
        pieChart = findViewById(R.id.pie_chart)
        val progressIcon = findViewById<ImageView>(R.id.personalprogress_progress_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)

        val categories = SharedData.lstCategories
        val entries = mutableListOf<PieEntry>()
        var totalHoursWorked = Duration.ZERO

        for(cat in SharedData.lstCategories) {
            totalHoursWorked += cat.hoursWorked
        }

        for (cats in SharedData.lstCategories) {
            val hoursWorked = cats.hoursWorked.toSeconds().toFloat()
            val percentage = (hoursWorked / totalHoursWorked.toSeconds().toFloat()) * 100
            entries.add(PieEntry(percentage, cats.strName))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = getColors(SharedData.lstCategories.size)

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()

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
    private fun getColors(categoryCount: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val colorTemplate = ColorTemplate.MATERIAL_COLORS

        for (i in 0 until categoryCount) {
            colors.add(colorTemplate[i % colorTemplate.size])
        }

        return colors
    }
}