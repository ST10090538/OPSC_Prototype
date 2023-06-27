package com.example.opscprototype

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Duration
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class HoursWorkedPage : AppCompatActivity(){
    private lateinit var LineChart: LineChart
    private var displayTasks = false
    private var filterStart: Date? = null
    private var filterEnd: Date? = null

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_hoursworked_page)
        LineChart = findViewById(R.id.lineChart)
        //setupLineChart()
        //displayLineChart()



        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)

















        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}