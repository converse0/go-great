package com.masuta.gogreat.presentation.new_training

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.MutableState
import java.text.SimpleDateFormat
import java.util.*

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
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            println("Date Picker$")
            val m = if (mMonth < 10) "0$mMonth" else mMonth
            val d = if (mDayOfMonth < 10) "0$mDayOfMonth" else mDayOfMonth
            date.value = "$mYear-$m-${d}T10:39:48.408Z"

        }, mYear, mMonth, mDay,
    )

    return mDatePickerDialog
}