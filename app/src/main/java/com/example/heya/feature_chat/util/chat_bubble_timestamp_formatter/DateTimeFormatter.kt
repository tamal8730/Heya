package com.example.heya.feature_chat.util.chat_bubble_timestamp_formatter

import com.example.heya.core.util.DateTime
import com.example.heya.core.util.TimestampFormatter

class DateTimeFormatter : TimestampFormatter {

    companion object {
        private val months = listOf(
            "jan",
            "feb",
            "mar",
            "apr",
            "may",
            "jun",
            "jul",
            "aug",
            "sep",
            "oct",
            "nov",
            "dec"
        )
    }

    private fun date(dateTime: DateTime): String = "${dateTime.day} ${months[dateTime.month]}"

    private fun time(dateTime: DateTime): String {
        val hourIn12Hr =
            if (dateTime.hour == 0) 12 else if (dateTime.hour <= 12) dateTime.hour else dateTime.hour - 12
        val amOrPm = if (dateTime.hour < 12) "am" else "pm"
        return "$hourIn12Hr:${dateTime.minute} $amOrPm"
    }

    override fun format(timestamp: String): String {
        val dateTime = DateTime.parseISO8601Timestamp(timestamp)
        return "${date(dateTime)}, ${time(dateTime)}"
    }

}