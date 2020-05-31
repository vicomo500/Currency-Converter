package com.vicomo.currencyconverter.repos

import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.utils.CurrencyException

interface CurrencyRepo {
    /*synchronous method*/
    @Throws(CurrencyException::class)
    suspend fun loadCurrencies(forceRemote: Boolean = false): List<Currency>
    /*async method*/
    @Throws(CurrencyException::class)
    fun loadExchangeRates(callback: ((CurrencyExchangeRate?) -> Unit)? = null, forceRemote: Boolean = false)
    //refresh callback
    fun registerExchangeRatesCallback(callback: (CurrencyExchangeRate?) -> Unit)
}