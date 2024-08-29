package com.vikravch.exchangeapp.currency_converter.data.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.vikravch.exchangeapp.currency_converter.domain.model.Transaction
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository

class TransactionHistoryPreferencesRepository(
    private val sharedPreferences: SharedPreferences
): TransactionsHistoryRepository {
    override fun addTransaction(transaction: Transaction) {
        Gson().toJson(transaction).let {
            sharedPreferences.edit().putStringSet("transactions",
                sharedPreferences.getStringSet("transactions", emptySet())?.plus(it) ?: setOf(it)).apply()
        }
    }

    override fun getAllTransactions(): Result<List<Transaction>> {
        return sharedPreferences.getStringSet("transactions", emptySet())?.map {
            Gson().fromJson(it, Transaction::class.java)
        }?.let {
            Result.success(it)
        } ?: Result.failure(IllegalArgumentException("No transactions found"))
    }

    override fun getNumberOfTransactions(): Result<Int> {
        return sharedPreferences.getStringSet("transactions", emptySet())?.size?.let {
            Result.success(it)
        } ?: Result.failure(IllegalArgumentException("No transactions found"))
    }
}