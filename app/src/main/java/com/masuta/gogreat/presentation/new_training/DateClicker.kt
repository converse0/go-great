package com.masuta.gogreat.presentation.new_training

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

/** convert Timestamp to Date*/
fun Long.toDate(): Date {
    return Date(this)
}


/** convert Date to String with format yyyy-MM-dd'T'HH:mm:ss.SSS */
fun formatDate(date: Date): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    return format.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun calendarTraining(
    date: MutableState<String>,
    context: Context
): DatePickerDialog {

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, mDayOfMonth: Int ->
            println("Date Picker$")
            val m = if (month < 10) "0$month" else month.toString()
            val d = if (mDayOfMonth < 10) "0$mDayOfMonth" else mDayOfMonth
            println("$year-$m-$d")
            val dateTime = LocalDateTime.now()
            val dateTime2 = LocalDateTime.of(year, Month.of(month+1), mDayOfMonth, dateTime.hour, dateTime.minute)
            date.value = dateTime2.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }, mYear, mMonth, mDay,
    )

    return mDatePickerDialog
}