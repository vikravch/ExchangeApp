package com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard

import CurrencySpinner
import CurrencySpinnerData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.vikravch.exchangeapp.currency_converter.R
import com.vikravch.exchangeapp.currency_converter.presentation.component.ConvertionAlertDialog
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
        },
        updateConvertionResultMessage = {
            viewModel.onEvent(ConversionDashboardEvent.UpdateConversionResultMessage(it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionDashboardPageUI(
    modifier: Modifier = Modifier,
    state: ConversionDashboardState = ConversionDashboardState(),
    submitConversion: (String, String, String) -> Unit = {_,_,_ ->},
    updateReceiveValue: (Double, String, String) -> Unit = {_,_,_ ->},
    resetBalances: () -> Unit = {},
    updateConvertionResultMessage: (String) -> Unit = {}
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
        Text(
            text = "MY BALANCES",
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        LazyRow(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            state.amountsStatus.forEach {
                item {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = "${it.value} ${it.currency} ",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Text(
            text = "CURRENCY EXCHANGE",
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Row(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).align(Alignment.CenterVertically)
                    .background(Color.Red, shape = RoundedCornerShape(50))
            ){
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.arrow_up),
                    contentDescription = "Arrow up",
                    tint = Color.White)
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                ),
                placeholder = {
                    Text(text = "0.0")
                }
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
            Box(
                modifier = Modifier.size(40.dp).align(Alignment.CenterVertically)
                    .background(Color.DarkGray, shape = RoundedCornerShape(50))
            ){
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = "Arrow up",
                    tint = Color.White)
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                text = "Receive")
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(100.dp)
                    .padding(horizontal = 8.dp),
                color = Color.Gray,
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
                .padding(top = 16.dp, bottom = 4.dp)
                .fillMaxWidth(),
            onClick = {
                submitConversion(amount, fromCurrency, toCurrency)
            }) {
            Text(text = "Submit")
        }
        Button(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                resetBalances()
            }) {
            Text(text = "Reset balances")
        }
    }

    if (state.conversionResultMessage.isNotEmpty()) {
         ConvertionAlertDialog(
             onDismissRequest = { updateConvertionResultMessage("") },
             onConfirmation = { updateConvertionResultMessage("") },
             dialogTitle = "Currency conversion",
             dialogText = state.conversionResultMessage,
             icon = Icons.Default.Done
         )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExchangeAppTheme {
        ConversionDashboardPageUI()
    }
}