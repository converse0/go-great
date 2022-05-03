package com.masuta.gogreat.data.store

import android.content.Context
import javax.inject.Inject

class StoreImpl @Inject constructor(
    private val context: Context
) : Store {

    private val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    override suspend fun getLocalCurrentExercise(): Int? {
        val value = sharedPref.getInt("currentExercise", -1)
        println("LOCAL EXERCISE: ${value}")
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExercise(indexExercise: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExercise", indexExercise ?: -1)
        editor.apply()
    }

    override suspend fun getLocalCurrentExerciseSets(): Int? {
        val value = sharedPref.getInt("currentExerciseSets", -1)
        println("LOCAL SETS: ${value}")
        return when (value) {
            -1 -> null
            else -> value
        }
    }

    override suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?) {
        val editor = sharedPref.edit()
        editor.putInt("currentExerciseSets", exerciseSets ?: -1)
        editor.apply()
    }

}