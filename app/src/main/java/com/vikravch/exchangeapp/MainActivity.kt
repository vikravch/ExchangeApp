package com.vikravch.exchangeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard.ConversionDashboardPage
import com.vikravch.exchangeapp.currency_converter.presentation.ui.theme.ExchangeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExchangeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConversionDashboardPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

