package com.masuta.gogreat.data.store

import com.masuta.gogreat.domain.model.LoginResponse

interface Store {

    suspend fun getLocalCurrentExercise(): Int?
    suspend fun setLocalCurrentExercise(indexExercise: Int?)

    suspend fun getLocalCurrentExerciseSets(): Int?
    suspend fun setLocalCurrentExerciseSets(exerciseSets: Int?)

    suspend fun setLocalToken(token: LoginResponse?)

}