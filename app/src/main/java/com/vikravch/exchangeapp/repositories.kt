package com.vikravch.exchangeapp

import android.icu.util.Currency
import kotlinx.coroutines.runBlocking

interface ExchangeRepository {
    suspend fun getRates(base: String): Map<String, Double>
}

class ExchangeFakeRepository: ExchangeRepository{
    override suspend fun getRates(base: String): Map<String, Double> {
        return mapOf("USD" to 1.2)
    }
}

interface AmountRepository {
    fun getAmounts(): Map<String, Double>
    fun getAmount(currency: String): Double
    fun setAmount(amount: Map<String, Double>)
}

class AmountFakeRepository: AmountRepository {
    private val amounts = mutableMapOf("EUR" to 1000.0)

    override fun getAmounts(): Map<String, Double> {
        return amounts
    }

    override fun getAmount(currency: String): Double {
        return amounts[currency] ?: 0.0
    }

    override fun setAmount(amount: Map<String, Double>) {
        amounts.putAll(amount)
    }
}

data class Transaction(
    val volume: Double,
    val fromCurrency: String,
    val toCurrency: String)

interface TransactionsHistoryRepository {
    fun addTransaction(transaction: Transaction)
    fun getAllTransactions(): Result<List<Transaction>>
    fun getNumberOfTransactions(): Result<Int>
}
class TransactionHistoryCollectionRepository: TransactionsHistoryRepository{
    private val database = mutableListOf<Transaction>()
    override fun addTransaction(transaction: Transaction) {
        database.add(transaction)
    }

    override fun getAllTransactions(): Result<List<Transaction>> {
        return Result.success(database)
    }

    override fun getNumberOfTransactions(): Result<Int> {
        return Result.success(database.size)
    }

}

class ConvertAmount(
    private val exchangeRepository: ExchangeRepository,
    private val amountRepository: AmountRepository,
    private val transactionsHistoryRepository: TransactionsHistoryRepository
) {
    suspend operator fun invoke(amount: Double, from: String, to: String): Result<String> {
        val amountInBaseCurrency = amountRepository.getAmount(from)
        if(amountInBaseCurrency < amount) {
            return Result.failure(IllegalArgumentException("Not enough money"))
        }

        val numberOfTransactions:Int = transactionsHistoryRepository.getNumberOfTransactions().getOrNull() ?: 0
        val feeAmount = getFeeWithNumberOfTransactions(numberOfTransactions)
        val rate = exchangeRepository.getRates(from)[to] ?: 1.0
        val convertedAmount = calculateConvertedAmount(amount, rate, feeAmount)

        val newAmountInBaseCurrency = amountInBaseCurrency - amount
        val newAmount = amountRepository.getAmount(to) + convertedAmount
        amountRepository.setAmount(mapOf(from to newAmountInBaseCurrency, to to newAmount))
        transactionsHistoryRepository.addTransaction(Transaction(amount, from, to))
        return Result.success("Updated!!");
    }

    private fun getFeeWithNumberOfTransactions(umberOfTransactions:Int): Double {
        return if(umberOfTransactions<7) 5.0 else 0.0
    }

    private fun calculateConvertedAmount(amount: Double, rate: Double, feeAmount: Double): Double {
        return (amount * rate)*(1-(feeAmount / 100))
    }
}



fun main(){
    val exchangeRepository = ExchangeFakeRepository()
    val amountRepository = AmountFakeRepository()
    val transactionsHistoryRepository = TransactionHistoryCollectionRepository()

    val convertAmount = ConvertAmount(
        exchangeRepository,
        amountRepository,
        transactionsHistoryRepository
    )
    runBlocking {
        println(convertAmount(200.0, "EUR", "USD"))
        println(amountRepository.getAmounts())
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(convertAmount(300.0, "EUR", "USD"))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
        println(convertAmount(500.0, "EUR", "USD"))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
        println(convertAmount(500.0, "EUR", "USD"))
        println(transactionsHistoryRepository.getAllTransactions().getOrNull())
        println(amountRepository.getAmounts())
    }
}