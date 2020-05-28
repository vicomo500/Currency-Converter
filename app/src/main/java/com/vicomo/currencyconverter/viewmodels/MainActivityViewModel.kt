package com.vicomo.currencyconverter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyAmount
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.repos.CurrencyException
import com.vicomo.currencyconverter.repos.CurrencyRepo
import com.vicomo.currencyconverter.utils.Data
import com.vicomo.currencyconverter.utils.ExchangeRateCalculator
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel
@Inject constructor(
    private val repo: CurrencyRepo
): ViewModel() {

    val currencies: MutableLiveData<Data<List<Currency>>> = MutableLiveData()

    val exchangeRates: MutableLiveData<Data<CurrencyExchangeRate?>> = MutableLiveData()

    val data: MutableLiveData<Data<List<CurrencyAmount>?>> = MutableLiveData()

    var exchangeRateCalculator: ExchangeRateCalculator? = null
    private set

    init {
        loadCurrencies()
        initExchangeRates()
    }

    private fun loadCurrencies(){
        currencies.value = Data.loading(currencies.value?.data)
        viewModelScope.launch {
            try{
                val values = repo.loadCurrencies()
                if(values.isEmpty())
                    currencies.value = Data.error(currencies.value?.data ?: listOf(), "Server returned empty data")
                else
                    currencies.value = Data.success(values)
            }catch (ex: CurrencyException){
                currencies.value = Data.error(currencies.value?.data ?: listOf(), ex.message ?: "unknown error")
            }
        }
    }

    private fun initExchangeRates(){
        exchangeRates.value = Data.loading(exchangeRates.value?.data)
        viewModelScope.launch {
            try {
                val callback: (CurrencyExchangeRate?) -> Unit = {
                    if(it == null)
                        exchangeRates.value = Data.error(exchangeRates.value?.data, "Server returned empty data")
                    else
                        exchangeRates.value = Data.success(it)
                    initCalculator()
                }
                repo.loadExchangeRates(callback)
                repo.registerExchangeRatesCallback(callback)
            } catch (ex: CurrencyException) {
                exchangeRates.value = Data.error(exchangeRates.value?.data, ex.message ?: "unknown error")
            }
        }
    }

    private fun initCalculator(){
        if( exchangeRates.value!!.data == null ||
            currencies.value!!.data == null ||
            currencies.value!!.data!!.isEmpty())
            return
        if(exchangeRateCalculator == null)
            exchangeRateCalculator = ExchangeRateCalculator( exchangeRates.value!!.data!!, currencies.value!!.data!!)
        else{
            exchangeRateCalculator!!.exchangeRates = exchangeRates.value!!.data!!
            exchangeRateCalculator!!.currencies = currencies.value!!.data!!
        }
    }

    fun calculate(amount: Double, currency: Currency) {
        if(exchangeRateCalculator == null){
            data.value = Data.error(null, "unable to calculate exchange rates")
            return
        }
        data.value = Data.loading(null)
        viewModelScope.launch {
            exchangeRateCalculator!!.calculate(amount, currency) {
                data.value = Data.success(it)
            }
        }
    }
}