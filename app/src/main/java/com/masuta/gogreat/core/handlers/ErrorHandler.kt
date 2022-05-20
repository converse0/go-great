package com.masuta.gogreat.core.handlers

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.masuta.gogreat.core.model.userToken
import com.masuta.gogreat.utils.Timeout
import com.masuta.gogreat.utils.Unauthenticated
import com.masuta.gogreat.utils.handleErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ErrorHandler(
    private val context: Context
) {

    suspend operator fun invoke(
        code: Int,
        message: String?,
//        context: Context,
        navController: NavHostController
    ) {

        when(val error = handleErrors(code)) {
            is Timeout -> {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            is Unauthenticated -> {
                withContext(Dispatchers.Main) {
                    userToken = null
                    navController.navigate(error.errRoute)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                withContext(Dispatchers.Main) {
                    navController.navigate(error.errRoute)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}