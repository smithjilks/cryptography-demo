package com.smithjilks.cryptographydemo.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.smithjilks.cryptographydemo.ui.screens.auth.AuthViewModel
import com.smithjilks.cryptographydemo.widgets.AppInputTextField
import timber.log.Timber

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
    ) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        HomeScreenContent(navController = navController, homeViewModel = homeViewModel)

    }
}

@Composable
fun HomeScreenContent(
    navController: NavController,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(true) {
        homeViewModel.generateAndPerformKeyExchange()
    }

    var pin by remember { homeViewModel.pin }
    val pinError by remember { homeViewModel.pinError }

    val isFormValid by remember {
        homeViewModel.isFormValid
    }

    val validatePinResult = homeViewModel.networkResult.collectAsState().value
    val setPinResult = homeViewModel.setPinNetworkResult.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                text = pin,
                isPassword = true,
                label = stringResource(id = R.string.pin_label),
                onTextChange = { pin = it },
                error = pinError
            )

            Button(
                onClick = {
                    homeViewModel.submit()
                },
                enabled = isFormValid,
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.btn_continue),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = {
                    homeViewModel.setPin()
                },
                enabled = isFormValid,
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.set_pin),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (validatePinResult != null) {
                val messageRes = when(validatePinResult) {
                    is NetworkResponse.Success -> R.string.pin_verification_success
                    else -> R.string.pin_verification_error
                }

                Text(
                    modifier = modifier.padding(16.dp),
                    text = stringResource(id = messageRes),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (setPinResult != null) {
                val messageRes = when(setPinResult) {
                    is NetworkResponse.Success -> R.string.set_pin_success
                    else -> R.string.set_pin_error
                }

                Text(
                    modifier = modifier.padding(16.dp),
                    text = stringResource(id = messageRes),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }

}