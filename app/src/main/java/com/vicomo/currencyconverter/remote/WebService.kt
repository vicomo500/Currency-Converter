package com.vicomo.currencyconverter.remote

import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.models.Currencies
import retrofit2.Call
import retrofit2.http.GET

interface WebService {
    @GET(EXCHANGE_RATE_PATH)
    fun getExchangeRates(): Call<CurrencyExchangeRate>
    @GET(CURRENCIES_PATH)
    fun getCurrencies(): Call<Currencies>
}