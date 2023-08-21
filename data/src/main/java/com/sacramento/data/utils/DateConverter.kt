package com.sacramento.data.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun convertDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date =   LocalDateTime.parse(dateString, formatter)
    val year = date.year
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val day = date.dayOfMonth
    return "$month $day, $year"
}