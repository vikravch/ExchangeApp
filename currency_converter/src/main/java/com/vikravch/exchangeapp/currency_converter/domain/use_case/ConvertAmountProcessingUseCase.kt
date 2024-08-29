package com.vikravch.exchangeapp.currency_converter.domain.use_case

import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository

class ConvertAmountProcessingUseCase(
    private val amountRepository: AmountRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    operator fun invoke(fromAmount: Double, convertedAmount: Double, from: String, to: String): Result<String> {
        val amountInBaseCurrency = amountRepository.getAmount(from)
        if(amountInBaseCurrency < fromAmount) {
            return Result.failure(IllegalArgumentException("Not enough money"))
        }

        val numberOfTransactions:Int = transactionsHistoryRepository.getNumberOfTransactions().getOrNull() ?: 0
        val feeAmount = getFeeWithNumberOfTransactions(numberOfTransactions)

        val commissionFee = fromAmount * (feeAmount / 100)
        val convertedAmountRounded= Math.round((convertedAmount)*100.0)/100.0
        val newAmountInBaseCurrency = Math.round((amountInBaseCurrency - fromAmount - commissionFee)*100.0)/100.0
        val newAmount = amountRepository.getAmount(to) + convertedAmountRounded
        amountRepository.setAmount(mapOf(from to newAmountInBaseCurrency, to to newAmount))

        transactionsHistoryRepository.addTransaction(Transaction(convertedAmountRounded, from, to))
        return if (feeAmount>0.1) Result.success("You have converted $fromAmount $from to $convertedAmountRounded $to Commission Fee - $commissionFee $from.")
                    else Result.success("You have converted $fromAmount $from to $convertedAmountRounded $to")
    }

    private fun getFeeWithNumberOfTransactions(numberOfTransactions:Int): Double {
        return if(numberOfTransactions>7) 5.0 else 0.0
    }
}