package com.imams.core.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun List<String>.asStringWithComma(): String {
    if (this.isEmpty()) return ""
    var label = ""
    this.forEachIndexed { i, s ->
        label = if (i == 0) s else "$label, $s"
    }
    return label
}

@SuppressLint("SimpleDateFormat")
fun String.simpleFormattedDate(): String {
    return try {
        val output = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        val date: Date = sdf.parse(this)
        return output.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}