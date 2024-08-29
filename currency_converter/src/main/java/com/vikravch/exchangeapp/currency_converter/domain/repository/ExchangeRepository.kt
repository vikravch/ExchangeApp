package com.vikravch.exchangeapp.currency_converter.domain.repository

import com.vikravch.exchangeapp.currency_converter.domain.model.Currency

interface ExchangeRepository {
    suspend fun getRates(base: String = "EUR"): Map<String, Currency>
}