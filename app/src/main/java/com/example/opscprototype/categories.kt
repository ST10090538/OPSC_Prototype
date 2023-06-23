package com.example.opscprototype

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class categories(strCatName: String) {
    @RequiresApi(Build.VERSION_CODES.O)
    var hoursWorked = Duration.ZERO
    val strName = strCatName
    var lstWorkLog: List<workLog>

    init {
        val initialWorkLog = workLog()
        val calendar = Calendar.getInstance()
        calendar.set(2000, 0, 1) // Set the year, month (0-indexed), and day
        initialWorkLog.dateWorked = calendar.time
        initialWorkLog.amountOfTimeWorked = Duration.ZERO
        lstWorkLog = listOf(initialWorkLog)
    }
}