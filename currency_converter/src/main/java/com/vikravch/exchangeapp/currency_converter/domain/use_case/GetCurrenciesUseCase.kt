package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Currency
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository

class GetCurrenciesUseCase(
    private val exchangeRepository: ExchangeRepository
) {
    suspend operator fun invoke(): Result<Map<String, Currency>> {
        val result = exchangeRepository.getRates()
        return result
    }
}