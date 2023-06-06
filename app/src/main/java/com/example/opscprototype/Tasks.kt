package com.example.opscprototype

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.util.Calendar
import java.util.Date

class Tasks (
    var strTaskName: String, var strCategory: String,
    var strDescription: String, var dtStartDate: Date, var dtEndDate: Date,
    var strStartTime: String, var strEndTime: String,
    var dblMinGoal: Double, var dblMaxGoal: Double, var imgPicture: Bitmap?, var timeSheet: String
){

    @RequiresApi(Build.VERSION_CODES.O)
    var durTimeWorked: Duration = Duration.ZERO
    var dateCreated: Date = Calendar.getInstance().time
    var lstWorkLog = emptyList<workLog>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCategoryHoursWorked(categoriesList: List<categories>) {
        val matchingCategories = categoriesList.filter { it.strName == strCategory }
        if (matchingCategories.isNotEmpty()) {
            val category = matchingCategories.first()
            category.hoursWorked = category.hoursWorked.plus(durTimeWorked)
        }
    }
}