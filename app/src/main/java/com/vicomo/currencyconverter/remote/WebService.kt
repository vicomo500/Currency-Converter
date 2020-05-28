package com.vicomo.currencyconverter.remote

import com.vicomo.currencyconverter.models.CurrenciesResp
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import retrofit2.http.GET
import retrofit2.Call

interface WebService {
    @GET(EXCHANGE_RATE_PATH)
    fun getExchangeRates(): Call<CurrencyExchangeRate>
    @GET(CURRENCIES_PATH)
    fun getCurrencies(): Call<CurrenciesResp>
}