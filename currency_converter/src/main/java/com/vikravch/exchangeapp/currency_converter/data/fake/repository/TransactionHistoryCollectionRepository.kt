package com.vikravch.exchangeapp.currency_converter.data.fake.repository

import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository

class TransactionHistoryCollectionRepository: TransactionsHistoryRepository {
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