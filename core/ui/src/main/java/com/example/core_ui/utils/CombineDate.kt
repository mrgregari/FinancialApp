package com.example.core_ui.utils

import java.util.Calendar
import java.util.Date

fun combineDateAndTime(date: Date, time: Date): Date {
    val calDate = Calendar.getInstance().apply { this.time = date }
    val calTime = Calendar.getInstance().apply { this.time = time }
    calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY))
    calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE))
    calDate.set(Calendar.SECOND, calTime.get(Calendar.SECOND))
    calDate.set(Calendar.MILLISECOND, calTime.get(Calendar.MILLISECOND))
    return calDate.time
}