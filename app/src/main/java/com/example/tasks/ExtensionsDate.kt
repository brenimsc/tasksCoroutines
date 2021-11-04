package com.example.tasks

import java.text.SimpleDateFormat

fun String.formatDateToDate(): String {
    val date = SimpleDateFormat("yyyy-MM-dd").parse(this)
    return SimpleDateFormat("dd/MM/yyyy").format(date)

}