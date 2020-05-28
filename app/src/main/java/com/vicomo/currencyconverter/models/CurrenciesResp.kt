package com.vicomo.currencyconverter.models


data class CurrenciesResp (
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)