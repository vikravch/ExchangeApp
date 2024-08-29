package com.vikravch.exchangeapp.currency_converter.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class GetCurrenciesDTO(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
)