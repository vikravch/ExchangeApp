package com.vikravch.exchangeapp.currency_converter.domain.use_case

data class CurrencyConverterUseCases(
    val convertAmountProcessingUseCase: ConvertAmountProcessingUseCase,
    val getAmountsUseCase: GetAmountsUseCase,
    val calculateConvertedFromEuroAmountUseCase: CalculateConvertedFromEuroAmountUseCase,
    val calculateConvertedToEuroAmountUseCase: CalculateConvertedToEuroAmountUseCase,
    val getCurrenciesUseCase: GetCurrenciesUseCase,
    val resetBalancesUseCase: ResetBalancesUseCase,
    val calculateUniversalUseCase: CalculateUniversalUseCase
)
