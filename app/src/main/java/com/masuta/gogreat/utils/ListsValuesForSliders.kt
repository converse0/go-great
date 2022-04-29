package com.masuta.gogreat.utils

import android.content.Context
import com.masuta.gogreat.R

class ListsValuesForSliders(
    private val context: Context
) {

    private var relaxMax: Int = 0
    private var relaxMin: Int = 0
    private var relaxStep: Int = 0

    private var durationMax: Int = 0
    private var durationMin: Int = 0
    private var durationStep: Int = 0

    init {
        context.resources.getInteger(R.integer.relax_max).let {
            relaxMax = it
        }
        context.resources.getInteger(R.integer.relax_min).let {
            relaxMin = it
        }
        context.resources.getInteger(R.integer.relax_step).let {
            relaxStep = it
        }

        context.resources.getInteger(R.integer.duration_max).let {
            durationMax = it
        }
        context.resources.getInteger(R.integer.duration_min).let {
            durationMin = it
        }
        context.resources.getInteger(R.integer.duration_step).let {
            durationStep = it
        }
    }

    fun getRelaxList(): List<Int> {
        val list: ArrayList<Int> = ArrayList()

        for (i in relaxMin..relaxMax step relaxStep) {
            list.add(i)
        }

        return list
    }

    fun getDurationList(): List<Int> {
        val list: ArrayList<Int> = ArrayList()

        for (i in durationMin..durationMax step durationStep) {
            list.add(i)
        }

        return list
    }
}