package com.example.opscprototype

import android.graphics.Bitmap
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
}