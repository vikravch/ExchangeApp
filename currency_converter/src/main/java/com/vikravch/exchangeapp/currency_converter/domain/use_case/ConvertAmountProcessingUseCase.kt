package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository

class ConvertAmountProcessingUseCase(
    private val amountRepository: AmountRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    operator fun invoke(convertedAmount: Double, from: String, to: String): Result<String> {
        val amountInBaseCurrency = amountRepository.getAmount(from)
        if(amountInBaseCurrency < convertedAmount) {
            return Result.failure(IllegalArgumentException("Not enough money"))
        }

        val numberOfTransactions:Int = transactionsHistoryRepository.getNumberOfTransactions().getOrNull() ?: 0
        val feeAmount = getFeeWithNumberOfTransactions(numberOfTransactions)

        val convertedAmountAfterFee = Math.round((convertedAmount - convertedAmount * (feeAmount / 100))*100.0)/100.0
        val newAmountInBaseCurrency = amountInBaseCurrency - convertedAmountAfterFee
        val newAmount = amountRepository.getAmount(to) + convertedAmountAfterFee
        amountRepository.setAmount(mapOf(from to newAmountInBaseCurrency, to to newAmount))
        transactionsHistoryRepository.addTransaction(Transaction(convertedAmountAfterFee, from, to))
        return Result.success("Updated!!")
    }

    private fun getFeeWithNumberOfTransactions(umberOfTransactions:Int): Double {
        return if(umberOfTransactions<7) 5.0 else 0.0
    }
}