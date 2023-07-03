package com.example.opscprototype

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HoursWorkedPage : AppCompatActivity() {
    private lateinit var filterStart: Date
    private lateinit var filterEnd: Date
    private lateinit var tasksSpinner: Spinner


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_hoursworked_page)

        // Initialize views
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        tasksSpinner = findViewById(R.id.spinner)
        BackButton_HoursWorkedPage()

        // Apply a filter start date
        findViewById<Button>(R.id.start_date).setOnClickListener {
            showDatePickerDialog(true)
        }

        // Apply a filter end date
        findViewById<Button>(R.id.end_date).setOnClickListener {
            showDatePickerDialog(false)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog(isFilterStartDate: Boolean) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)

                val formattedDate = SimpleDateFormat("yyyy/MM/dd").format(calendar.time)
                if (isFilterStartDate) {
                    filterStart = calendar.time
                    findViewById<Button>(R.id.start_date).text = formattedDate
                } else {
                    filterEnd = calendar.time
                    findViewById<Button>(R.id.end_date).text = formattedDate
                }

                if (isFilterStartDate == false) {
                    populateTasksSpinner()
                }

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateTasksSpinner() {
        // Filter the tasks based on the selected dates
        val filteredTasks = getFilteredTasks()

        // Create an adapter and set it to the spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filteredTasks.map { it.strTaskName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tasksSpinner.adapter = adapter

        tasksSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTask = filteredTasks[position]
                updateGraph(selectedTask)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateGraph(selectedTask: Tasks) {
        val lineChart = findViewById<LineChart>(R.id.lineChart)

        lineChart.clear()

        val legend = lineChart.legend
        legend.textColor = Color.WHITE

        val entriesMinGoal = mutableListOf<Entry>()
        val entriesMaxGoal = mutableListOf<Entry>()
        val entriesActualTime = mutableListOf<Entry>()

        val startDate = filterStart
        val endDate = filterEnd
        val minGoal = selectedTask.dblMinGoal.toFloat()
        val maxGoal = selectedTask.dblMaxGoal.toFloat()

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        dateFormat.timeZone = TimeZone.getDefault()
        while (!calendar.time.after(endDate) || calendar.time == endDate) {
            val currentDate = dateFormat.format(calendar.time)

            val workLogEntry = selectedTask.lstWorkLog.find { workLog ->
                val logDate = dateFormat.format(workLog.dateWorked)
                logDate == currentDate
            }

            val timeWorked =
                workLogEntry?.amountOfTimeWorked?.toMillis()?.toFloat()?.div(1000 * 3600) ?: 0f

            entriesMinGoal.add(Entry(calendar.timeInMillis.toFloat(), minGoal))
            entriesMaxGoal.add(Entry(calendar.timeInMillis.toFloat(), maxGoal))
            entriesActualTime.add(Entry(calendar.timeInMillis.toFloat(), timeWorked))

            calendar.add(Calendar.DAY_OF_MONTH, 1)


        }

        val dataSetMinGoal = LineDataSet(entriesMinGoal, "Min Goal")
        dataSetMinGoal.color = ColorTemplate.COLORFUL_COLORS[0]
        dataSetMinGoal.setDrawValues(false)
        dataSetMinGoal.enableDashedLine(10f, 10f, 0f)
        dataSetMinGoal.lineWidth = 2f

        val dataSetMaxGoal = LineDataSet(entriesMaxGoal, "Max Goal")
        dataSetMaxGoal.color = ColorTemplate.COLORFUL_COLORS[1]
        dataSetMaxGoal.setDrawValues(false)
        dataSetMaxGoal.enableDashedLine(10f, 10f, 0f)
        dataSetMaxGoal.lineWidth = 2f

        val dataSetActualTime = LineDataSet(entriesActualTime, "Actual Time")
        dataSetActualTime.color = ColorTemplate.COLORFUL_COLORS[2]
        dataSetActualTime.setDrawValues(false)
        dataSetActualTime.lineWidth = 2f

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(dataSetMinGoal)
        dataSets.add(dataSetMaxGoal)
        dataSets.add(dataSetActualTime)
        val data = LineData(dataSets)

        lineChart.data = data
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.WHITE
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val millis = value.toLong()
                val date = Date(millis)
                return dateFormat.format(date)
            }
        }

        val yAxis: YAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(false)
        yAxis.textColor = Color.WHITE

        lineChart.axisRight.isEnabled = false

        lineChart.invalidate()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    private fun getFilteredTasks(): List<Tasks> {
        val tasksList: List<Tasks> = SharedData.lstTasks
        val filteredTasks: MutableList<Tasks> = mutableListOf()

        for (task in tasksList) {
            var hasMatchingWorkLog = false

            for (workLog in task.lstWorkLog) {
                val logDate = workLog.dateWorked
                if (logDate >= filterStart && logDate <= filterEnd) {
                    hasMatchingWorkLog = true
                    break
                }
            }

            if (hasMatchingWorkLog) {
                filteredTasks.add(task)
            }
        }

        return filteredTasks
    }

    private fun BackButton_HoursWorkedPage() {

        val click = findViewById<View>(R.id.BackButton_HoursWorkedPage)
        click.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }
    }
}