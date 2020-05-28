package com.vicomo.currencyconverter.fakes

import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.persistence.ExchangeRateDao

class FakeExchangeRateDao(
    private var exchangeRate: CurrencyExchangeRate?
): ExchangeRateDao {

    override fun save(exchangeRate: CurrencyExchangeRate) {
        this.exchangeRate = exchangeRate
    }

    override fun load(): CurrencyExchangeRate? = exchangeRate

    override fun clear(): Int{
        exchangeRate = null
        return 1
    }
}