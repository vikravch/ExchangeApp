package com.vikravch.exchangeapp.currency_converter.domain.use_case

data class CurrencyConverterUseCases(
    val convertAmountUseCase: ConvertAmountUseCase,
    val getAmountsUseCase: GetAmountsUseCase,
    val calculateConvertedAmountUseCase: CalculateConvertedAmountUseCase,
    val calculateConvertedToEuroAmountUseCase: CalculateConvertedToEuroAmountUseCase,
    val getCurrenciesUseCase: GetCurrenciesUseCase,
    val resetBalancesUseCase: ResetBalancesUseCase
)
