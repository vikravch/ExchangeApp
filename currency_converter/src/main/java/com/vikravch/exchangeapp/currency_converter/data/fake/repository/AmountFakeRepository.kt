package com.vikravch.exchangeapp.currency_converter.data.fake.repository

import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository

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

    override fun clearAmount() {
        amounts.clear()
    }
}