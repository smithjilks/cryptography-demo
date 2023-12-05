package com.smithjilks.cryptographydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smithjilks.cryptographydemo.navigation.AppNavigation
import com.smithjilks.cryptographydemo.ui.screens.splashscreen.ConfigsViewModel
import com.smithjilks.cryptographydemo.ui.theme.CryptographyDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ConfigsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initialize()
        setContent {
            CryptographyDemoApp()
        }
    }
}

@Composable
fun CryptographyDemoApp() {
    CryptographyDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppNavigation()
            }
        }
    }
}

