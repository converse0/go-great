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

    private var setsMax: Int = 0
    private var setsMin: Int = 0
    private var setsStep: Int = 0

    private var countsMax: Int = 0
    private var countsMin: Int = 0
    private var countsStep: Int = 0

    private var repetitionsMax: Int = 0
    private var repetitionsMin: Int = 0
    private var repetitionsStep: Int = 0

    val getRelaxList = getList(relaxMin, relaxMax, relaxStep)
    val getDurationList = getList(durationMin, durationMax, durationStep)
    val getStepsList = getList(setsMin, setsMax, setsStep)
    val getCountsList = getList(countsMin, countsMax, countsStep)
    val getRepetitionsList = getList(repetitionsMin, repetitionsMax, repetitionsStep)

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
        // Duration
        context.resources.getInteger(R.integer.duration_max).let {
            durationMax = it
        }
        context.resources.getInteger(R.integer.duration_min).let {
            durationMin = it
        }
        context.resources.getInteger(R.integer.duration_step).let {
            durationStep = it
        }
        // Sets
        context.resources.getInteger(R.integer.sets_max).let {
            setsMax = it
        }
        context.resources.getInteger(R.integer.sets_min).let {
            setsMin = it
        }
        context.resources.getInteger(R.integer.sets_step).let {
            setsStep = it
        }
        // Counts
        context.resources.getInteger(R.integer.counts_max).let {
            countsMax = it
        }
        context.resources.getInteger(R.integer.counts_min).let {
            countsMin = it
        }
        context.resources.getInteger(R.integer.counts_step).let {
            countsStep = it
        }
        // Repetitions
        context.resources.getInteger(R.integer.repetitions_max).let {
            repetitionsMax = it
        }
        context.resources.getInteger(R.integer.repetitions_min).let {
            repetitionsMin = it
        }
        context.resources.getInteger(R.integer.repetitions_step).let {
            repetitionsStep = it
        }
    }

    private fun getList(min: Int, max: Int, step: Int): List<Int> {
        val list: ArrayList<Int> = ArrayList()

        for (i in min..max step step) {
            list.add(i)
        }

        return list
    }
}