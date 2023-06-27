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
import com.github.mikephil.charting.formatter.ValueFormatter
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
import java.util.Locale
import java.util.*


class HoursWorkedPage : AppCompatActivity() {
    private lateinit var LineChart: LineChart
    private var displayTasks = false
    private var filterStart: Date? = null
    private var filterEnd: Date? = null

    lateinit var lineChart: LineChart
    private val lstTasks = listOf("Task A", "Task B", "Task C") // Replace with your task data
    private val lstWorklog = listOf(4f, 6f, 8f) // Replace with your worklog data

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_hoursworked_page)
        LineChart = findViewById(R.id.lineChart)
        setupLineChart()
        displayLineChart()
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)



        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }

    private fun setupLineChart() {
        // Configure chart settings
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false

        // Customize X-axis
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = DayAxisValueFormatter()

        // Customize Y-axis
        val yAxis: YAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.axisMinimum = 0f

        lineChart.axisRight.isEnabled = false
    }

    private fun displayLineChart() {
        val entries: MutableList<Entry> = ArrayList()
        for (i in lstWorklog.indices) {
            entries.add(Entry(i.toFloat(), lstWorklog[i]))
        }

        val lineDataSet = LineDataSet(entries, "Total Hours Worked")
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ColorTemplate.getHoloBlue()
        lineDataSet.color = ColorTemplate.getHoloBlue()
        lineDataSet.lineWidth = 2f
        lineDataSet.valueTextSize = 12f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    inner class DayAxisValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index >= 0 && index < lstTasks.size) {
                dateFormat.format(Date()) // Replace with your date logic
            } else {
                ""
            }
        }
    }
}

