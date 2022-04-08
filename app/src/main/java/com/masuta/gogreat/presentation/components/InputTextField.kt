package com.masuta.gogreat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputTextField(
    text: String,
    value: String,
    keyboardController: SoftwareKeyboardController?,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    onChangeValue: (String) -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Bold
    )
    OutlinedTextField(
        value = value,
        onValueChange = { onChangeValue(it) },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    onChangeValue("")
                }
            },
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        )
    )
}