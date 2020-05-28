package com.vicomo.currencyconverter.utils

import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyAmount
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.repos.CurrencyException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ExchangeRateCalculator (
    var exchangeRates: CurrencyExchangeRate,
    var currencies: List<Currency>,
    private val dispatchers: CoroutineDispatcher = Dispatchers.IO
){
    suspend fun calculate(amt: Double, currency: Currency, callback: (List<CurrencyAmount>) -> Unit) =
        withContext(dispatchers){
            try{
                //convert amount to source currency
                val sourceAmt = convertToSourceAmt(amt, currency)
                val list = arrayListOf<CurrencyAmount>()
                for (entry: Map.Entry<String, Double> in exchangeRates.quotes){
                    val entryCurrency = findCurrency(entry.key) ?: continue
                    list.add(CurrencyAmount( sourceAmt * entry.value, entryCurrency ))
                }
                callback.invoke(list)
            }catch (ex: Exception){
                callback.invoke(listOf())
            }
    }

    private fun convertToSourceAmt(amt: Double, currency: Currency): Double{
        // 1 source currency = this currency's rate
        val rate = exchangeRates.quotes[currency.code] ?: kotlin.run { throw CurrencyException("currency does not exist") }
        // x source currency = ?
        return amt / rate
    }
    private fun findCurrency(code: String): Currency? =
        currencies.find { it.code == code }
}