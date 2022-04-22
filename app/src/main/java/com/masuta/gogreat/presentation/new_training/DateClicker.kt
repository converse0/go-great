package com.masuta.gogreat.presentation.new_training

import android.app.DatePickerDialog
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


@Composable
fun CalendarTraining(
    date: MutableState<String>,
    showCal: MutableState<Boolean>
){

    val mContext = LocalContext.current

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

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mYear-$mMonth-${mDayOfMonth}T$mHour:$mMinute:$mSecond.408Z"

        }, mYear, mMonth, mDay,
    )

    if (showCal.value) {
        mDatePickerDialog.show()
    }
}