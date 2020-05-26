package com.vicomo.currencyconverter.repos

import com.vicomo.currencyconverter.persistence.CurrenciesDao
import com.vicomo.currencyconverter.persistence.ExchangeRateDao
import com.vicomo.currencyconverter.remote.WebService
import javax.inject.Inject

class CurrencyRepoImpl
    @Inject constructor(
        private val webService: WebService,
        private val currenciesDao: CurrenciesDao,
        private val exchangeRateDao: ExchangeRateDao
): CurrencyRepo {

}