package com.example.core_data.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
fun Date.toApiStringStartOfDay(): String {
    println("toApiStringStartOfDay called for $this")
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    val result = format.format(calendar.time)
    println("toApiStringStartOfDay result: $result")
    return result
}

@SuppressLint("SimpleDateFormat")
fun Date.toApiStringEndOfDay(): String {
    println("toApiStringEndOfDay called for $this")
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    val result = format.format(calendar.time)
    println("toApiStringEndOfDay result: $result")
    return result
}

@SuppressLint("SimpleDateFormat")
fun Date.toServerApiString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toLocalApiStringStartOfDay(): String {
    val calendar = Calendar.getInstance() // локальная временная зона
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    // format.timeZone = TimeZone.getDefault() // можно явно указать локальную зону
    return format.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun Date.toLocalApiStringEndOfDay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    // format.timeZone = TimeZone.getDefault()
    return format.format(calendar.time)
}