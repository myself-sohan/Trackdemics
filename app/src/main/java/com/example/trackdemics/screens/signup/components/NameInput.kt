package com.example.trackdemics.screens.signup.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.trackdemics.widgets.InputField

@Composable
fun NameInput(
    modifier: Modifier = Modifier.Companion,
    nameState: MutableState<String>,
    labelId : String,
    enabled: Boolean,
    imeAction: ImeAction = ImeAction.Companion.Next,
    onAction: KeyboardActions = KeyboardActions.Companion.Default
)
{
    InputField(
        modifier = modifier,
        valueState = nameState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Companion.Text,
        imeAction = imeAction,
        onAction = onAction,
    )
}