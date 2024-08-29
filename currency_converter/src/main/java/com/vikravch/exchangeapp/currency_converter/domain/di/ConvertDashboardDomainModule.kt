package com.vikravch.exchangeapp.currency_converter.domain.di
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateConvertedAmountUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateConvertedToEuroAmountUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.ConvertAmountUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CurrencyConverterUseCases
import com.vikravch.exchangeapp.currency_converter.domain.use_case.GetAmountsUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.GetCurrenciesUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.ResetBalancesUseCase
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
    fun provideConvertAmountUseCase(
        amountRepository: AmountRepository,
        exchangeRepository: ExchangeRepository,
        transactionsHistoryRepository: TransactionsHistoryRepository
    ): CurrencyConverterUseCases {
        return CurrencyConverterUseCases(
            convertAmountUseCase = ConvertAmountUseCase(
                amountRepository,
                transactionsHistoryRepository
            ),
            getAmountsUseCase = GetAmountsUseCase(amountRepository),
            calculateConvertedAmountUseCase = CalculateConvertedAmountUseCase(),
            calculateConvertedToEuroAmountUseCase = CalculateConvertedToEuroAmountUseCase(),
            getCurrenciesUseCase = GetCurrenciesUseCase(
                exchangeRepository
            ),
            resetBalancesUseCase = ResetBalancesUseCase(amountRepository)
        )
    }
}