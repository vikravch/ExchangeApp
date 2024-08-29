package com.vikravch.exchangeapp.currency_converter.data.di


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.vikravch.exchangeapp.currency_converter.data.NetworkInfo
import com.vikravch.exchangeapp.currency_converter.data.NetworkInfoImpl
import com.vikravch.exchangeapp.currency_converter.data.preferences.AmountPreferencesRepository
import com.vikravch.exchangeapp.currency_converter.data.preferences.TransactionHistoryPreferencesRepository
import com.vikravch.exchangeapp.currency_converter.data.remote.CurrencyApi
import com.vikravch.exchangeapp.currency_converter.data.remote.ExchangeServerRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConvertDashboardModule {

    @Provides
    @Singleton
    fun provideNetworkInfo(context: Context): NetworkInfo {
        return NetworkInfoImpl(context)
    }

        @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor {
                        message ->
                    Timber.tag("currency_api").d(message)
                }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(client: OkHttpClient): CurrencyApi {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
            .baseUrl("https://developers.paysera.com/tasks/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRepository(
        api: CurrencyApi,
        networkInfo: NetworkInfo
    ): ExchangeRepository {
        return ExchangeServerRepository(
            currencyApi = api,
            networkInfo = networkInfo
        )
    }

    @Provides
    @Singleton
    fun provideAmountRepository(
        sharedPreferences: SharedPreferences
    ): AmountRepository {
        return AmountPreferencesRepository(sharedPreferences)
    }

   @Provides
    @Singleton
    fun provideTransactionHistoryRepository(
        sharedPreferences: SharedPreferences
    ): TransactionsHistoryRepository {
        return TransactionHistoryPreferencesRepository(sharedPreferences)
    }

}