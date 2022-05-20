package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputTextField(
    text: String,
    value: String,
    keyboardController: SoftwareKeyboardController?,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    enabled: Boolean = true,
//    focusManager: FocusManager,
    imeAction: ImeAction = ImeAction.Next,
    onChangeValue: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
    OutlinedTextField(
        value = value,
        textStyle = TextStyle(Color.Black),
        onValueChange = { onChangeValue(it) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None ,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        )
    )
}