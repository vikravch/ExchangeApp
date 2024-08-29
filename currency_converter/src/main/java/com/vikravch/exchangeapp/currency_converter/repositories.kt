package com.vikravch.exchangeapp.currency_converter

import com.vikravch.exchangeapp.currency_converter.data.fake.repository.AmountFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.ExchangeFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.TransactionHistoryCollectionRepository
import com.vikravch.exchangeapp.currency_converter.domain.use_case.ConvertAmountUseCase
import kotlinx.coroutines.runBlocking

fun main(){
    val exchangeRepository = ExchangeFakeRepository()
    val amountRepository = AmountFakeRepository()
    val transactionsHistoryRepository = TransactionHistoryCollectionRepository()

    val convertAmountUseCase = ConvertAmountUseCase(
        amountRepository,
        transactionsHistoryRepository
    )
    runBlocking {
        println(convertAmountUseCase(200.0, 1.0,"EUR", "USD", 200.0))
        println(amountRepository.getAmounts())
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(convertAmountUseCase(300.0, 1.0, "EUR", "GBP", 200.0))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
        println(convertAmountUseCase(500.0, 1.0, "EUR", "USD", 200.0))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
        println(convertAmountUseCase(500.0, 1.0, "EUR", "GBP", 200.0))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
    }
}