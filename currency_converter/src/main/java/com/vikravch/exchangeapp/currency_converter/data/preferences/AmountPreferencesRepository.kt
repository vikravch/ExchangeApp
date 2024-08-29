package com.vikravch.exchangeapp.currency_converter.data.preferences

import android.content.SharedPreferences
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import timber.log.Timber

class AmountPreferencesRepository(
    private val sharedPreferences: SharedPreferences
): AmountRepository {
    override fun getAmounts(): Map<String, Double> {
        val resultMap = mutableMapOf<String, Double>()
        val stringSet = sharedPreferences.getStringSet("amounts", emptySet())
        Timber.tag("AmountPreferencesRepository").d("getAmounts: " + stringSet)
        stringSet?.forEach { itemFromPreference ->
            val itemsForAdd = itemFromPreference.split(":")
            resultMap[itemsForAdd[0]] = itemsForAdd[1].toDoubleOrNull()?:0.0
        }
        Timber.tag("AmountPreferencesRepository").d("getAmounts: " + resultMap)
        return resultMap
    }

    override fun getAmount(currency: String): Double {
        return getAmounts()[currency] ?: 0.0
    }

    override fun setAmount(amount: Map<String, Double>) {
        val currentAmount = getAmounts().toMutableMap()
        Timber.tag("AmountPreferencesRepository").d("setAmount before: " + currentAmount)
        currentAmount.putAll(amount)
        Timber.tag("AmountPreferencesRepository").d("setAmount after put: " + currentAmount)
        val amounts = currentAmount.entries.map{ entry -> "${entry.key}:${entry.value}" }.toSet()
        Timber.tag("AmountPreferencesRepository").d("setAmount: " + amounts)
        sharedPreferences.edit().putStringSet("amounts",amounts).apply()
    }

    override fun clearAmount() {
        sharedPreferences.edit().remove("amounts").apply()
    }
}