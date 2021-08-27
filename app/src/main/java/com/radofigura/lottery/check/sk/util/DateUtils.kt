package com.radofigura.lottery.check.sk.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentFormattedTime() = getFormattedTime(getCurrentTime())
    fun getTimeWithFormat(time: Date, format: String) = getFormatter(format).format(time)
    fun getFormattedTime(time: Date) = getFormatter("dd.MMMM.yyyy HH:mm:ss").format(time)
    fun getFormatter(format: String) = SimpleDateFormat(format, Locale("sk")).apply {
        timeZone = TimeZone.getTimeZone("CET")
    }
    fun getCurrentTime() = Calendar.getInstance().time
    fun parseTime(date: String) = SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSSZ", Locale("sk")).parse(date)

    fun shouldReceiveData(): Boolean {
        val currentTime = Calendar.getInstance()

        if (currentTime.get(Calendar.DAY_OF_WEEK) == Constants.SHOW_ONLINE_DAY_OF_WEEK) {
            val todayTime = "${currentTime.get(Calendar.HOUR_OF_DAY)}:${currentTime.get(Calendar.MINUTE)}:${currentTime.get(Calendar.SECOND)}"
            currentTime.time = SimpleDateFormat("HH:mm:ss", Locale("sk")).parse(todayTime)!!

            val showStartedTimeDate = SimpleDateFormat("HH:mm:ss", Locale("sk")).parse(Constants.SHOW_START_TIME)!!
            val showStartedTime = Calendar.getInstance()
            showStartedTime.time = showStartedTimeDate

            val showEndedTime = Calendar.getInstance()
            showEndedTime.time = showStartedTimeDate
            showEndedTime.add(Calendar.HOUR, Constants.SHOW_DURATION_HOURS)

            if (currentTime.after(showStartedTime) && currentTime.before(showEndedTime)) {
                return true
            }
        }

        return false
    }

    fun getTimeInMinutesAndSeconds(time: Long) = SimpleDateFormat("mm:ss.SSS", Locale("sk")).format(Date(time))
}