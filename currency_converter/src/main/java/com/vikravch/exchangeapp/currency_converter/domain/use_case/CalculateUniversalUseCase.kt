package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Currency

class CalculateUniversalUseCase{
    operator fun invoke(amount: Double, currencies: Map<String, Currency>, fromCurrency: String, toCurrency: String): Double{
        val calculateConvertedFromEuroAmountUseCase = CalculateConvertedFromEuroAmountUseCase()
        val calculateConvertedToEuroAmountUseCase = CalculateConvertedToEuroAmountUseCase()

        return when {
            fromCurrency == "EUR" -> {
                calculateConvertedFromEuroAmountUseCase(
                    amount = amount,
                    rate = currencies[toCurrency]?.rate?: 0.0,
                    feeAmount = 0.0
                )
            }
            toCurrency == "EUR" -> {
                calculateConvertedToEuroAmountUseCase(
                    amount = amount,
                    rate = currencies[fromCurrency]?.rate?: 0.0,
                    feeAmount = 0.0
                )
            }
            else -> {
                val amountEUR = calculateConvertedToEuroAmountUseCase(
                    amount = amount,
                    rate = currencies[fromCurrency]?.rate?: 0.0,
                    feeAmount = 0.0
                )
                calculateConvertedFromEuroAmountUseCase(
                    amount = amountEUR,
                    rate = currencies[toCurrency]?.rate?: 0.0,
                    feeAmount = 0.0
                )
            }
        }
    }
}