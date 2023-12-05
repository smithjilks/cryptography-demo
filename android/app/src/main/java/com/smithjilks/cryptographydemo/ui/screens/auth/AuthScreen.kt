package com.smithjilks.cryptographydemo.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.R
import com.smithjilks.cryptographydemo.ui.screens.AppScreens
import com.smithjilks.cryptographydemo.widgets.AppInputTextField

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    AuthScreenContent(navController = navController, authViewModel = authViewModel)
}


@Composable
fun AuthScreenContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    var email by remember { authViewModel.email }
    var password by remember { authViewModel.password }

    val emailError by remember { authViewModel.emailError }
    val passwordError by remember { authViewModel.passwordError }

    val isFormValid by remember {
        authViewModel.isFormValid
    }

    val result = authViewModel.networkResult.collectAsState().value

    if (result is NetworkResponse.Success) {
        LaunchedEffect(true ) {
            navController.navigate(AppScreens.HomeScreen.name)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = modifier.size(100.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .shadow(4.dp)
                .background(
                    Color.White.copy(
                        .8f
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            AppInputTextField(
                modifier = modifier,
                text = email,
                label = stringResource(id = R.string.email_label),
                onTextChange = { email = it },
                error = emailError
            )

            AppInputTextField(
                modifier = modifier,
                text = password,
                label = stringResource(id = R.string.password_label),
                isPassword = true,
                onTextChange = { password = it },
                error = passwordError
            )

            Button(
                onClick = {
                    authViewModel.submit()
                },
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                ),
                enabled = isFormValid
            ) {
                Text(
                    text = stringResource(id = R.string.btn_continue),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (result !is NetworkResponse.Success && result != null) {
                Text(
                    modifier = modifier.padding(16.dp),
                    text = stringResource(id = R.string.auth_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }

}