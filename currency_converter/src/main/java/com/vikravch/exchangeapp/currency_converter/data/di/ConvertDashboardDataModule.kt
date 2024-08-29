package com.vikravch.exchangeapp.currency_converter.data.di


import android.content.SharedPreferences
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.AmountFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.ExchangeFakeRepository
import com.vikravch.exchangeapp.currency_converter.data.fake.repository.TransactionHistoryCollectionRepository
import com.vikravch.exchangeapp.currency_converter.data.preferences.AmountPreferencesRepository
import com.vikravch.exchangeapp.currency_converter.data.preferences.TransactionHistoryPreferencesRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConvertDashboardModule {

    @Provides
    @Singleton
    fun provideAmountRepository(
        sharedPreferences: SharedPreferences
    ): AmountRepository {
        return AmountPreferencesRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideExchangeRepository(): ExchangeRepository {
        return ExchangeFakeRepository()
    }

   @Provides
    @Singleton
    fun provideTransactionHistoryRepository(
        sharedPreferences: SharedPreferences
    ): TransactionsHistoryRepository {
        return TransactionHistoryPreferencesRepository(sharedPreferences)
    }

}