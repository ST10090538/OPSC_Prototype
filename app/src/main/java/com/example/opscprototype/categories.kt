package com.example.opscprototype

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration

class categories (strCatName: String){
    @RequiresApi(Build.VERSION_CODES.O)
    var hoursWorked = Duration.ZERO
    val strName = strCatName
}