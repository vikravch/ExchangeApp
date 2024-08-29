package com.vikravch.exchangeapp.currency_converter.data.remote

import com.vikravch.exchangeapp.currency_converter.data.remote.dto.GetCurrenciesDTO
import com.vikravch.exchangeapp.currency_converter.domain.model.Currency

fun GetCurrenciesDTO.toCurrencyMap(): Map<String, Currency> {
    return rates.mapValues { Currency(code = it.key, rate = it.value) }
}