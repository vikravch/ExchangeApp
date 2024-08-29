package com.vikravch.exchangeapp.currency_converter.data.preferences

import android.content.SharedPreferences
import android.util.Log
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository

class AmountPreferencesRepository(
    private val sharedPreferences: SharedPreferences
): AmountRepository {
    override fun getAmounts(): Map<String, Double> {
        val resultMap = mutableMapOf<String, Double>()
        val stringSet = sharedPreferences.getStringSet("amounts", emptySet())
        Log.d("AmountPreferencesRepository", "getAmounts: $stringSet")
        stringSet?.forEach { itemFromPreference ->
            val itemsForAdd = itemFromPreference.split(":")
            resultMap[itemsForAdd[0]] = itemsForAdd[1].toDoubleOrNull()?:0.0
        }
        Log.d("AmountPreferencesRepository", "getAmounts: $resultMap")
        return resultMap
    }

    override fun getAmount(currency: String): Double {
        return getAmounts()[currency] ?: 0.0
    }

    override fun setAmount(amount: Map<String, Double>) {
        val currentAmount = getAmounts().toMutableMap()
        Log.d("AmountPreferencesRepository", "setAmount before: $currentAmount")
        currentAmount.putAll(amount)
        Log.d("AmountPreferencesRepository", "setAmount after put: $currentAmount")
        val amounts = currentAmount.entries.map{ entry -> "${entry.key}:${entry.value}" }.toSet()
        Log.d("AmountPreferencesRepository", "setAmount: $amounts")
        sharedPreferences.edit().putStringSet("amounts",amounts).apply()
    }

    override fun clearAmount() {
        sharedPreferences.edit().remove("amounts").apply()
    }
}