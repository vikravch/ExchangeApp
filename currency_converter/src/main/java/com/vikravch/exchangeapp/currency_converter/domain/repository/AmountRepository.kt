package com.vikravch.exchangeapp.currency_converter.domain.repository

interface AmountRepository {
    fun getAmounts(): Map<String, Double>
    fun getAmount(currency: String): Double
    fun setAmount(amount: Map<String, Double>)
    fun clearAmount()
}