package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository

class ResetBalancesUseCase(
    private val amountRepository: AmountRepository,
) {
    operator fun invoke() {
        amountRepository.clearAmount()
        amountRepository.setAmount(mapOf("EUR" to 1000.0))
    }
}