package com.vikravch.exchangeapp.currency_converter.data.remote

import com.vikravch.exchangeapp.currency_converter.data.NetworkInfo
import com.vikravch.exchangeapp.currency_converter.data.NoInternetException
import com.vikravch.exchangeapp.currency_converter.data.ServerException
import com.vikravch.exchangeapp.currency_converter.data.toServerError
import com.vikravch.exchangeapp.currency_converter.domain.model.Currency
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import retrofit2.HttpException

class ExchangeServerRepository(
    private val currencyApi: CurrencyApi,
    private val networkInfo: NetworkInfo
): ExchangeRepository {
    override suspend fun getRates(base: String): Result<Map<String, Currency>> {
        return if (networkInfo.isConnected()) {
            try {
                val response = currencyApi.getCurrencies()
                return if (response.isSuccessful){
                    response.body()?.let {
                        Result.success(it.toCurrencyMap())
                    } ?:
                    Result.success(emptyMap())
                } else {
                    Result.failure(ServerException("Server error"))
                }
            } catch (e: HttpException) {
                Result.failure(ServerException(e.toServerError().message))
            }
        } else {
            Result.failure(NoInternetException())
        }
    }
}