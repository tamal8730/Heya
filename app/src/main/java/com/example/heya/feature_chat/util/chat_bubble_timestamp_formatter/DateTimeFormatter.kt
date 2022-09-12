package com.example.heya.feature_chat.util.chat_bubble_timestamp_formatter

import com.example.heya.core.util.DateTime
import com.example.heya.core.util.TimestampFormatter

class DateTimeFormatter : TimestampFormatter {

    companion object {
        private fun pad(num: Int): String = if (num < 10) "0$num" else num.toString()
    }

    private fun date(dateTime: DateTime): String =
        "${pad(dateTime.day)}/${pad(dateTime.month)}/${dateTime.year}"

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