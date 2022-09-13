package com.example.heya.feature_find_peer.util.last_message_timestamp_formatter

import com.example.heya.core.util.DateTime
import com.example.heya.core.util.TimestampFormatter

/**
 * Converts ISO8601 timestamp to either of the following formats:
 * 1) "hh:mm am/pm", if the timestamp is of today
 * 2) "yesterday", if the timestamp is of yesterday
 * 3) "dd/mm/yyyy", in all other cases
 */
class TimeOrDateTimestampFormatter : TimestampFormatter {

    companion object {

        private fun pad(num: Int): String = if (num < 10) "0$num" else num.toString()

        private fun date(dateTime: DateTime): String =
            "${dateTime.day}/${dateTime.month}/${dateTime.year}"

        private fun time(dateTime: DateTime): String {
            val hourIn12Hr =
                if (dateTime.hour == 0) 12 else if (dateTime.hour <= 12) dateTime.hour else dateTime.hour - 12
            val amOrPm = if (dateTime.hour < 12) "am" else "pm"
            return "$hourIn12Hr:${pad(dateTime.minute)} $amOrPm"
        }

        private fun isYesterday(dateTime: DateTime, now: DateTime = DateTime.now()): Boolean {
            return false
        }
    }

    override fun format(timestamp: String): String {
        val now = DateTime.now()
        val inputDateTime = DateTime.parseISO8601Timestamp(timestamp)

        //if today
        return if (now.year == inputDateTime.year && now.month == inputDateTime.month && now.day == inputDateTime.day) {
            time(inputDateTime)
        }
        //if yesterday
        else if (isYesterday(inputDateTime, now)) {
            "yesterday"
        } else {
            date(inputDateTime)
        }

    }

}