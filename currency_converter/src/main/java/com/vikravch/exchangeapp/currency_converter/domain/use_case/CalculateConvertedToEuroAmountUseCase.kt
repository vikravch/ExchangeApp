package com.vikravch.exchangeapp.currency_converter.domain.use_case

class CalculateConvertedToEuroAmountUseCase {
    operator fun invoke(amount: Double, rate: Double, feeAmount: Double): Double{
        return (amount / rate)*(1-(feeAmount / 100))
    }
}