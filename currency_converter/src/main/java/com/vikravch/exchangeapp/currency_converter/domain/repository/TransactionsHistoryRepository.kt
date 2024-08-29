package com.vikravch.exchangeapp.currency_converter.domain.repository

import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction

interface TransactionsHistoryRepository {
    fun addTransaction(transaction: Transaction)
    fun getAllTransactions(): Result<List<Transaction>>
    fun getNumberOfTransactions(): Result<Int>
}