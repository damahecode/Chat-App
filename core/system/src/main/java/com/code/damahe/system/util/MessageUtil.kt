package com.code.damahe.system.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Date


object MessageUtil {

    fun convertTimeMillis(millis: Long): String {
        val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()
        val date = Date(millis)

//        val day = simpleDateFormat.calendar.get(Calendar.DAY_OF_WEEK)
        val relative = DateUtils.getRelativeTimeSpanString(date.time)

        return relative.toString() // simpleDateFormat.format(date)
    }
}