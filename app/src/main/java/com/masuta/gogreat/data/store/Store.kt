package com.masuta.gogreat.data.store

interface Store {

    suspend fun getLocalCurrentExercise(): Int?
    suspend fun setLocalCurrentExercise(indexExercise: Int?)

    suspend fun getLocalCurrentExerciseSets(): Int?
    suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?)

}