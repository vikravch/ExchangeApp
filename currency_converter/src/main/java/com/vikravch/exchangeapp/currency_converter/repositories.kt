package com.vikravch.exchangeapp.currency_converter

import com.vikravch.exchangeapp.currency_converter.data.fake.repository.AmountFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.ExchangeFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.TransactionHistoryCollectionRepository
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateUniversalUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.ConvertAmountProcessingUseCase
import kotlinx.coroutines.runBlocking

fun main(){
    val exchangeRepository = ExchangeFakeRepository()
    val amountRepository = AmountFakeRepository()
    val transactionsHistoryRepository = TransactionHistoryCollectionRepository()

    val convertAmountProcessingUseCase = ConvertAmountProcessingUseCase(
        amountRepository,
        transactionsHistoryRepository
    )
    val calculateUniversalUseCase = CalculateUniversalUseCase()
    runBlocking {
        println(convertAmountProcessingUseCase(200.0, 200.0,"EUR", "USD"))
        println(amountRepository.getAmounts())
        val rates = exchangeRepository.getRates().getOrNull()?: emptyMap()
        println("1000 EUR to USD - "+
            calculateUniversalUseCase(
                1000.0,
                rates,
                "EUR",
                "USD"
            )
        )
        println("1000 USD to EUR - "+
            calculateUniversalUseCase(
                1000.0,
                rates,
                "USD",
                "EUR"
            )
        )
        println("1000 GBP to USD - "+
            calculateUniversalUseCase(
                1000.0,
                rates,
                "GBP",
                "USD"
            )
        )
    }
}