package com.vikravch.exchangeapp.currency_converter.domain.model

data class Transaction(
    val volume: Double,
    val fromCurrency: String,
    val toCurrency: String)