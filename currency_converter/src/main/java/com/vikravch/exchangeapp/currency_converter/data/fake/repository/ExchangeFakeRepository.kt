package com.vikravch.exchangeapp.currency_converter.data.fake.repository

import com.vikravch.exchangeapp.currency_converter.domain.model.Currency
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository

class ExchangeFakeRepository: ExchangeRepository {
    override suspend fun getRates(base: String): Map<String, Currency> {
        return mapOf("USD" to Currency("USD", 1.2), "GBP" to Currency("GBP", 0.8))
    }
}