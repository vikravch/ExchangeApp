package com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard

import CurrencySpinner
import CurrencySpinnerData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.vikravch.exchangeapp.currency_converter.presentation.ui.theme.ExchangeAppTheme
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun ConversionDashboardPage(
    modifier: Modifier = Modifier,
    viewModel: ConversionDashboardViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ConversionDashboardEvent.GetAmounts)
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is ConversionDashboardViewModel.UiEvent.ConversionSuccess -> {
                    Timber.tag("ConversionDashboardPage").d("Conversion success: " + it.message)
                    viewModel.onEvent(ConversionDashboardEvent.GetAmounts)
                }
                is ConversionDashboardViewModel.UiEvent.ConversionError -> {
                    Timber.tag("ConversionDashboardPage").d("Conversion error: " + it.message)
                    viewModel.onEvent(ConversionDashboardEvent.GetAmounts)
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentLifecycleState = lifecycleOwner.lifecycle.currentStateAsState()

    LaunchedEffect(key1 =  currentLifecycleState.value) {
        while(currentLifecycleState.value.isAtLeast(Lifecycle.State.STARTED)) {
            viewModel.onEvent(ConversionDashboardEvent.GetCurrencies)
            delay(5000)
        }
    }

    ConversionDashboardPageUI(
        modifier = modifier,
        state = viewModel.state,
        submitConversion = {
                amount, fromCurrency, toCurrency ->
            Timber.tag("ConversionDashboardPage")
                .d("amount: " + amount + ", fromCurrency: " + fromCurrency + ", toCurrency: " + toCurrency)
            viewModel.onEvent(
                ConversionDashboardEvent.ConvertAmount(
                    amount.toDoubleOrNull()?:0.0,
                    fromCurrency, toCurrency))
        },
        updateReceiveValue = { value, fromCurrency, toCurrency ->
            viewModel.onEvent(ConversionDashboardEvent.UpdateReceiveValue(value, fromCurrency, toCurrency))
        },
        resetBalances = {
            viewModel.onEvent(ConversionDashboardEvent.ResetBalances)
        }
    )
}

@Composable
fun ConversionDashboardPageUI(
    modifier: Modifier = Modifier,
    state: ConversionDashboardState = ConversionDashboardState(),
    submitConversion: (String, String, String) -> Unit = {_,_,_ ->},
    updateReceiveValue: (Double, String, String) -> Unit = {_,_,_ ->},
    resetBalances: () -> Unit = {}
) {
    val (amount, setAmount) = remember { mutableStateOf("") }
    val (fromCurrency, setFromCurrency) = remember { mutableStateOf("EUR") }
    val (toCurrency, setToCurrency) = remember { mutableStateOf("USD") }

    val currencySpinnerData = state.currencies.map { CurrencySpinnerData(it.key, it.key) }.toMutableList()
    currencySpinnerData.add(0, CurrencySpinnerData("EUR", "EUR"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "MY BALANCES")
        LazyRow(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            state.amountsStatus.forEach {
                item {
                    Text(text = "${it.currency} ${it.value} ")
                }
            }
        }
        Text(text = "CURRENCY EXCHANGE")
        Row(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = "Sell")
            TextField(
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 8.dp),
                value = amount,
                onValueChange = {
                    setAmount(it)
                    updateReceiveValue(it.toDoubleOrNull()?:0.0, fromCurrency, toCurrency)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            CurrencySpinner(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(100.dp),
                list = currencySpinnerData,
                preselected = CurrencySpinnerData(fromCurrency,fromCurrency),
                onSelectionChanged = {
                    setFromCurrency(it.name)
                    updateReceiveValue(amount.toDoubleOrNull()?:0.0, it.name, toCurrency)
                },
            )
        }
        Row(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = "Receive")
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(100.dp)
                    .padding(horizontal = 8.dp),
                text = state.receiveVolume)
            CurrencySpinner(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(100.dp),
                list = currencySpinnerData,
                preselected = CurrencySpinnerData(toCurrency,toCurrency),
                onSelectionChanged = {
                    setToCurrency(it.name)
                    updateReceiveValue(amount.toDoubleOrNull()?:0.0, fromCurrency, it.name)
                },
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                submitConversion(amount, fromCurrency, toCurrency)
            }) {
            Text(text = "Submit")
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                resetBalances()
            }) {
            Text(text = "Reset balances")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExchangeAppTheme {
        ConversionDashboardPageUI()
    }
}