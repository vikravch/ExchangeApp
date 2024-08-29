package com.vikravch.exchangeapp.currency_converter.data.remote

import com.vikravch.exchangeapp.currency_converter.data.remote.dto.GetCurrenciesDTO
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("/tasks/api/currency-exchange-rates")
    suspend fun getCurrencies():Response<GetCurrenciesDTO>
}


