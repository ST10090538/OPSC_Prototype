package com.example.opscprototype

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Time
import java.time.Duration
import java.util.Date

class Tasks (
    strTaskName: String, strCategory: String,
    strDescription: String, dtStartDate: Date, dtEndDate: Date,
    strStartTime: String, strEndTime: String, dblMinGoal: Double, dblMaxGoal: Double, imgPicture: Bitmap?
){
    var dblMaxGoal = dblMaxGoal
    var strEndTime = strEndTime
    var strStartTime = strStartTime
    var dblMinGoal = dblMinGoal
    var dtEndDate = dtEndDate
    var dtStartDate = dtStartDate
    var strDescription = strDescription
    var strCategory = strCategory
    var strTaskName = strTaskName
    var imgPicture = imgPicture
    @RequiresApi(Build.VERSION_CODES.O)
    var durTimeWorked = Duration.ZERO

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCategoryHoursWorked(categoriesList: List<categories>) {
        val matchingCategories = categoriesList.filter { it.strName == strCategory }
        if (matchingCategories.isNotEmpty()) {
            val category = matchingCategories.first()
            category.hoursWorked = category.hoursWorked.plus(durTimeWorked)
        }
    }
}