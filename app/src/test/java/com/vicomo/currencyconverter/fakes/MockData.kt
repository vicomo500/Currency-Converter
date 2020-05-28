package com.vicomo.currencyconverter.fakes

import com.vicomo.currencyconverter.models.CurrenciesResp
import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyExchangeRate

val cachedCurrencies = arrayListOf(
    Currency("USD", "United States Dollar", 1),
    Currency("GBP", "British Pound Sterling", 2),
    Currency("EUR", "Euro", 3),
    Currency("JPY", "Japanese Yen", 4),
    Currency("NGN", "Nigerian Naira", 5)
)
val cachedExchangeRates = CurrencyExchangeRate(
    1,
    "111111111",
    "USD",
    mapOf(
        Pair("USD", 1.0),
        Pair("GBP", 0.534567),
        Pair("EUR", 1.917021),
        Pair("JPY", 101.650385)
    )
)

val remoteExchangeRates = CurrencyExchangeRate(
    1,
    "22222222",
    "USD",
    mapOf(
        Pair("USD", 1.0),
        Pair("GBP", 0.821959),
        Pair("EUR", 0.917011),
        Pair("JPY", 107.650385),
        Pair("NGN", 390.503727)
    )
)

val remoteCurrenciesMap = mapOf(
    Pair("AUD", "Australian Dollar"),
    Pair("GBP", "British Pound Sterling"),
    Pair("EUR", "Euro"),
    Pair("ERR", "Erroneous Currency"),
    Pair("JPY", "Japanese Yen"),
    Pair("NGN", "Nigerian Naira"),
    Pair("USD", "United States Dollar"),
    Pair("ZAR", "South African Rand")
)
val remoteCurrencyResp = CurrenciesResp(
    true,
    "http://example.com/terms",
    "http://example.com/privacy",
    remoteCurrenciesMap
)

const val SERVER_ERROR_MSG = "server error"