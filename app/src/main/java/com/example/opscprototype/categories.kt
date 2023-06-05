package com.example.opscprototype

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration

class categories (strCatName: String){
    private val categories: MutableList<categories> = mutableListOf()
    @RequiresApi(Build.VERSION_CODES.O)
    var hoursWorked = Duration.ZERO
    val strName = strCatName
    fun addCategory(category: categories) {
        categories.add(category)
    }

    fun getCategories(): List<categories> {
        return categories.toList()
    }




}