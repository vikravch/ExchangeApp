package com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard

sealed class ConversionDashboardEvent {
    data object GetAmounts : ConversionDashboardEvent()
    data class ConvertAmount(
        val amount: Double, val fromCurrency: String, val toCurrency: String
    ) : ConversionDashboardEvent()
    data class UpdateReceiveValue(
        val value: Double, val fromCurrency: String, val toCurrency: String
    ) : ConversionDashboardEvent()

    data object GetCurrencies : ConversionDashboardEvent()
    data object ResetBalances : ConversionDashboardEvent()
}