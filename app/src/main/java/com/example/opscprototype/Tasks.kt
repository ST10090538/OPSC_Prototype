package com.example.opscprototype

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.util.Calendar
import java.util.Date

class Tasks (
    var strTaskName: String, var strCategory: String,
    var strDescription: String, var dtStartDate: Date, var dtEndDate: Date,
    var strStartTime: String, var strEndTime: String,
    var dblMinGoal: Double, var dblMaxGoal: Double, var timeSheet: String
){

    @RequiresApi(Build.VERSION_CODES.O)
    var durTimeWorked: Duration = Duration.ZERO
    var dateCreated: Date = Calendar.getInstance().time
    var lstWorkLog = emptyList<workLog>()
    var imgPicture: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCategoryHoursWorked(categoriesList: List<categories>) {
        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
        val matchingCategories = categoriesList.filter { it.strName == strCategory }
        if (matchingCategories.isNotEmpty()) {
            val category = matchingCategories.first()
            val catRef = database.getReference(SharedData.currentUser).child("categories").child(category.strName)
            catRef.child("hoursWorked").child("seconds").setValue(category.hoursWorked.plus(durTimeWorked).seconds.toInt())
            category.hoursWorked = category.hoursWorked.plus(durTimeWorked)

        }
    }
}