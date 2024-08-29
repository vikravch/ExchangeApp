package com.vikravch.exchangeapp.currency_converter.presentation.page.conversion_dashboard

import com.vikravch.exchangeapp.currency_converter.domain.model.Amount
import com.vikravch.exchangeapp.currency_converter.domain.model.Currency


data class ConversionDashboardState(
    val amountsStatus: List<Amount> = emptyList(),
    val currencies: Map<String, Currency> = emptyMap(),
    val receiveVolume: String = "0.0",
)
