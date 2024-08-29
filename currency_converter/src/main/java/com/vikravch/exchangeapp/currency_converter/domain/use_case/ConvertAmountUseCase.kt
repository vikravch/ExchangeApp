package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository

class ConvertAmountUseCase(
    private val amountRepository: AmountRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    suspend operator fun invoke(amount: Double, rate: Double, from: String, to: String, amountEUR: Double): Result<String> {
        val calculateConvertedAmountUseCase = CalculateConvertedAmountUseCase()
        val amountInBaseCurrency = amountRepository.getAmount(from)
        if(amountInBaseCurrency < amount) {
            return Result.failure(IllegalArgumentException("Not enough money"))
        }

        val numberOfTransactions:Int = transactionsHistoryRepository.getNumberOfTransactions().getOrNull() ?: 0
        val feeAmount = getFeeWithNumberOfTransactions(numberOfTransactions)
        /*val rate = exchangeRepository.getRates(from)[to]?.rate ?: 1.0*/
        val convertedAmount = calculateConvertedAmountUseCase(amountEUR, rate, feeAmount)

        val newAmountInBaseCurrency = amountInBaseCurrency - amount
        val newAmount = amountRepository.getAmount(to) + convertedAmount
        amountRepository.setAmount(mapOf(from to newAmountInBaseCurrency, to to newAmount))
        transactionsHistoryRepository.addTransaction(Transaction(amount, from, to))
        return Result.success("Updated!!")
    }

    private fun getFeeWithNumberOfTransactions(umberOfTransactions:Int): Double {
        return if(umberOfTransactions<7) 5.0 else 0.0
    }
}