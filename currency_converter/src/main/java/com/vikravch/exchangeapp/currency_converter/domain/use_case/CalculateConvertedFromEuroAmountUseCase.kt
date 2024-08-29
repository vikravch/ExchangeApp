package com.vikravch.exchangeapp.currency_converter.domain.use_case

class CalculateConvertedFromEuroAmountUseCase {
    operator fun invoke(amount: Double, rate: Double, feeAmount: Double): Double{
        return Math.round((amount * rate)*(1-(feeAmount / 100))*100.0)/100.0
    }
}