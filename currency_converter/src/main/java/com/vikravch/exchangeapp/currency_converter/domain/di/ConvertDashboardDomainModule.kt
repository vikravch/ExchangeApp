package com.vikravch.exchangeapp.currency_converter.domain.di
import com.vikravch.exchangeapp.currency_converter.domain.repository.AmountRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.ExchangeRepository
import com.vikravch.exchangeapp.currency_converter.domain.repository.TransactionsHistoryRepository
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateConvertedFromEuroAmountUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateConvertedToEuroAmountUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.CalculateUniversalUseCase
import com.vikravch.exchangeapp.currency_converter.domain.use_case.ConvertAmountProcessingUseCase
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
            convertAmountProcessingUseCase = ConvertAmountProcessingUseCase(
                amountRepository,
                transactionsHistoryRepository
            ),
            getAmountsUseCase = GetAmountsUseCase(amountRepository),
            calculateConvertedFromEuroAmountUseCase = CalculateConvertedFromEuroAmountUseCase(),
            calculateConvertedToEuroAmountUseCase = CalculateConvertedToEuroAmountUseCase(),
            getCurrenciesUseCase = GetCurrenciesUseCase(
                exchangeRepository
            ),
            resetBalancesUseCase = ResetBalancesUseCase(amountRepository),
            calculateUniversalUseCase = CalculateUniversalUseCase()
        )
    }
}