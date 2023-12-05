package com.smithjilks.cryptographydemo.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppInputTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    @StringRes error: Int? = null,
    isPassword: Boolean = false,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardControlController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        isError = error != null && text.isNotEmpty(),
        enabled = isEnabled,
        readOnly = isReadOnly,
        maxLines = maxLines,
        label = {
            Text(text = label)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardControlController?.hide()
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp)
    )

    if (error != null && text.isNotEmpty()) {
        Text(
            text = stringResource(id = error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(2.dp).fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }

}