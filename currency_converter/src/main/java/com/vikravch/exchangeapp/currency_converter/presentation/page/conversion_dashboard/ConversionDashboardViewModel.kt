package com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CurrencyConverterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConversionDashboardViewModel @Inject constructor(
   private val currencyConverterUseCases: CurrencyConverterUseCases
): ViewModel() {
    var state by mutableStateOf(ConversionDashboardState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ConversionDashboardEvent) {
        when (event) {
            is ConversionDashboardEvent.GetAmounts -> {
                viewModelScope.launch {
                    val amounts = currencyConverterUseCases.getAmountsUseCase().getOrNull()?: emptyList()
                    state = state.copy(amountsStatus = amounts)
                }
            }
            is ConversionDashboardEvent.ConvertAmount -> {
                viewModelScope.launch {
                    val amountAfterConvertion = currencyConverterUseCases.calculateUniversalUseCase(
                        amount = event.amount,
                        fromCurrency = event.fromCurrency,
                        toCurrency = event.toCurrency,
                        currencies = state.currencies
                    )

                    val conversionResponse = currencyConverterUseCases.convertAmountProcessingUseCase(
                        fromAmount = event.amount,
                        from = event.fromCurrency,
                        to = event.toCurrency,
                        convertedAmount = amountAfterConvertion
                    )
                    if (conversionResponse.isSuccess) {
                        state = state.copy(conversionResultMessage = conversionResponse.getOrNull()?: "")
                        val amounts = currencyConverterUseCases.getAmountsUseCase().getOrNull()?: emptyList()
                        state = state.copy(amountsStatus = amounts)
                    } else {
                        state = state.copy(conversionResultMessage = conversionResponse.exceptionOrNull()?.message?: "")
                    }

                }
            }

            is ConversionDashboardEvent.UpdateReceiveValue -> {
                viewModelScope.launch {
                    val amountAfterConvertion = currencyConverterUseCases.calculateUniversalUseCase(
                        amount = event.value,
                        fromCurrency = event.fromCurrency,
                        toCurrency = event.toCurrency,
                        currencies = state.currencies
                    )
                    Timber.tag("ConversionDashboardViewModel").d("currencies - " + state.currencies)
                    Timber.tag("ConversionDashboardViewModel")
                        .d("Conversion result: " + amountAfterConvertion + " from " + event.fromCurrency + " to " + event.toCurrency + " " + "amount: " + amountAfterConvertion + " value: " + event.value)
                    state = state.copy(receiveVolume = "+$amountAfterConvertion")
                }
            }

            ConversionDashboardEvent.GetCurrencies ->{
                viewModelScope.launch {
                    val currencies = currencyConverterUseCases.getCurrenciesUseCase().getOrNull()?: emptyMap()
                    state = state.copy(currencies = currencies)
                }
            }

            ConversionDashboardEvent.ResetBalances -> {
                viewModelScope.launch {
                    currencyConverterUseCases.resetBalancesUseCase()
                    val amounts = currencyConverterUseCases.getAmountsUseCase().getOrNull()?: emptyList()
                    state = state.copy(amountsStatus = amounts)
                }
            }

            is ConversionDashboardEvent.UpdateConversionResultMessage -> {
                state = state.copy(conversionResultMessage = event.message)
            }
        }
    }

    sealed class UiEvent {
        data class ConversionSuccess(val message: String): UiEvent()
        data class ConversionError(val message: String): UiEvent()
    }
}