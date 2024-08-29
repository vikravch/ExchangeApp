package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Amount
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository

class GetAmountsUseCase(
    private val amountRepository: AmountRepository
) {
    suspend operator fun invoke(): Result<List<Amount>> {
        val amounts = amountRepository.getAmounts()
        if(amounts.isEmpty()) {
            return Result.failure(IllegalArgumentException("No amounts found"))
        }
        return amounts.entries.map { Amount(it.key, it.value) }.let {
            Result.success(it)
        }
    }
}