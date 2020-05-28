package com.vicomo.currencyconverter.fakes

import com.vicomo.currencyconverter.models.CurrenciesResp
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.remote.WebService
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.Calls
import java.io.IOException

class FakeWebService(
    var currencies: CurrenciesResp?,
    var exchangeRates: CurrencyExchangeRate?,
    private val delegate: BehaviorDelegate<WebService>,
    var shouldFail: Boolean = false
): WebService {

    override fun getExchangeRates(): Call<CurrencyExchangeRate> {
        return if(shouldFail)
                Calls.failure(Exception(SERVER_ERROR_MSG))
            else
                delegate.returningResponse(exchangeRates).getExchangeRates()
    }

    override fun getCurrencies(): Call<CurrenciesResp> {
        return if(shouldFail)
            Calls.failure(Exception(SERVER_ERROR_MSG))
        else
            delegate.returningResponse(currencies).getCurrencies()
    }
}