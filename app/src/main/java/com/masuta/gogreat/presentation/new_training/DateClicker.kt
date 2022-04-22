package com.masuta.gogreat.presentation.new_training

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


//@Composable
fun calendarTraining(
    date: MutableState<String>,
    context: Context
): DatePickerDialog {

    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mHour: Int
    val mMinute: Int
    val mSecond: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    mMinute = mCalendar.get(Calendar.MINUTE)
    mSecond = mCalendar.get(Calendar.SECOND)

    mCalendar.time = Date()

//    mCalendar.get

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