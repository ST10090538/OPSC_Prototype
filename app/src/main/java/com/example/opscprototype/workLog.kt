package com.example.opscprototype

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.util.Calendar

class workLog {
    var dateWorked = Calendar.getInstance().time
    @RequiresApi(Build.VERSION_CODES.O)
    var amountOfTimeWorked = Duration.ZERO
}