package com.masuta.gogreat.presentation.new_training

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.MutableState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.*

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
            val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
            val now = Clock.System.now()
            val h = now.toLocalDateTime(timeZone).hour
            val min = now.toLocalDateTime(timeZone).minute
            val s = now.toLocalDateTime(timeZone).second
            date.value = LocalDateTime(
                year,
                month + 1,
                mDayOfMonth,
                h,
                min,
                s,
                0
            ).toInstant(timeZone = timeZone).toString()
        },
        mYear, mMonth, mDay,
    )

    return mDatePickerDialog
}