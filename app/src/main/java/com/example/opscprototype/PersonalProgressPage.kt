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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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

class PersonalProgressPage : AppCompatActivity(), OnChartValueSelectedListener {
    private lateinit var pieChart: PieChart
    private var displayTasks = false
    private var filterStart: Date? = null
    private var filterEnd: Date? = null

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_personalprogress_page)
        pieChart = findViewById(R.id.pie_chart)

        val progressIcon = findViewById<ImageView>(R.id.personalprogress_progress_icon)
        val profileIcon = findViewById<ImageView>(R.id.personalprogress_profile_icon)
        val timesheetIcon = findViewById<ImageView>(R.id.personalprogress_timesheet_icon)
        BackButton_PersonalProgressPage()

        val entries = mutableListOf<PieEntry>()
        var totalHoursWorked = Duration.ZERO

        for (cat in SharedData.lstCategories) {
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
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()

        pieChart.setOnChartValueSelectedListener(this)

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        findViewById<Button>(R.id.btnStartDate).setOnClickListener(){
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0) // Set time to 23:59:59
                    filterStart = calendar.time

                    val formattedDate = SimpleDateFormat("yyyy/MM/dd").format(filterStart)
                    val btnEndDate = findViewById<Button>(R.id.btnStartDate)
                    btnEndDate.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.btnEndDate).setOnClickListener() {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selectedYear, selectedMonth, selectedDay, 23, 59, 59) // Set time to 23:59:59
                    filterEnd = calendar.time

                    val formattedDate = SimpleDateFormat("yyyy/MM/dd").format(filterEnd)
                    val btnEndDate = findViewById<Button>(R.id.btnEndDate)
                    btnEndDate.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.btnApplyFilter).setOnClickListener(){
            val filteredEntries = mutableListOf<PieEntry>()
            var totalFilteredHoursWorked = Duration.ZERO
            val start = filterStart
            val end = filterEnd
            for (cat in SharedData.lstCategories) {
                var filterCatHoursWorked = Duration.ZERO
                val strCatName = cat.strName
                for (log in cat.lstWorkLog) {
                    if ((log.dateWorked.after(filterStart) || log.dateWorked == filterStart) &&
                        (log.dateWorked.before(filterEnd) || log.dateWorked == filterEnd)) {
                        filterCatHoursWorked += log.amountOfTimeWorked
                    }
                }
                if (filterCatHoursWorked > Duration.ZERO) {
                    totalFilteredHoursWorked += filterCatHoursWorked
                    val hoursWorked = filterCatHoursWorked.toSeconds().toFloat()
                    val percentage = (hoursWorked / totalHoursWorked.toSeconds().toFloat()) * 100
                    filteredEntries.add(PieEntry(percentage, strCatName))
                }
            }

            val dataSet = PieDataSet(filteredEntries, "")
            dataSet.sliceSpace = 3f
            dataSet.selectionShift = 5f
            dataSet.colors = getColors(filteredEntries.size)

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter(pieChart))
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.BLACK)

            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.invalidate()
        }

// Get the chart's description object
        val description = Description()
        description.text = ""
        description.textColor = Color.WHITE
// Set the color of the description labels to white
        pieChart.description = description
        pieChart.description.textColor = Color.WHITE
        pieChart.description = description
// Set the text color of the legend labels to white
        val legend = pieChart.legend
        legend.textColor = Color.WHITE

    }

    private fun getColors(categoryCount: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val colorTemplate = ColorTemplate.MATERIAL_COLORS

        for (i in 0 until categoryCount) {
            colors.add(colorTemplate[i % colorTemplate.size])
        }

        return colors
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e != null && e is PieEntry) {
            val selectedCategory = e.label
            val tasksInCategory = getTasksInCategory(selectedCategory)

            if (filterStart != null && filterEnd != null) {
                val filteredTasks = tasksInCategory.filter { task ->
                    task.lstWorkLog.any { log ->
                        ((log.dateWorked.after(filterStart) || log.dateWorked == filterStart) &&
                                (log.dateWorked.before(filterEnd) || log.dateWorked == filterEnd))
                    }
                }
                updatePieChart(filteredTasks)
            } else {
                updatePieChart(tasksInCategory)
            }

            displayTasks = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onNothingSelected() {
        if (displayTasks) {
            SharedData.lstCategories
            val entries = mutableListOf<PieEntry>()
            var totalHoursWorked = Duration.ZERO

            for (cat in SharedData.lstCategories) {
                totalHoursWorked += cat.hoursWorked
            }

            filterStart = null
            filterEnd = null
            findViewById<Button>(R.id.btnStartDate).text = "Start Date"
            findViewById<Button>(R.id.btnEndDate).text = "End Date"

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
            data.setValueTextColor(Color.WHITE)

            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.invalidate()

            displayTasks = false
        }
    }

    private fun getTasksInCategory(category: String): List<Tasks> {
        return SharedData.lstTasks.filter { it.strCategory == category }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updatePieChart(tasks: List<Tasks>) {
        val entries = mutableListOf<PieEntry>()
        var totalHoursWorked = Duration.ZERO

        for (task in tasks) {
            totalHoursWorked += task.durTimeWorked
        }

        for (task in tasks) {
            val hoursWorked = task.durTimeWorked.toSeconds().toFloat()
            val percentage = (hoursWorked / totalHoursWorked.toSeconds().toFloat()) * 100
            entries.add(PieEntry(percentage, task.strTaskName))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 30f
        dataSet.selectionShift = 3f
        dataSet.colors = getColors(tasks.size)

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    private fun BackButton_PersonalProgressPage() {

        val click = findViewById<View>(R.id.BackButton_PersonalProgressPage)
        click.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }
    }
}