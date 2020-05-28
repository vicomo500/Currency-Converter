package com.vicomo.currencyconverter.repos

import androidx.annotation.VisibleForTesting
import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import com.vicomo.currencyconverter.persistence.CurrenciesDao
import com.vicomo.currencyconverter.persistence.ExchangeRateDao
import com.vicomo.currencyconverter.remote.WebService
import com.vicomo.currencyconverter.utils.convertCurrencyMapToList
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CurrencyRepoImpl
    @Inject constructor(
        private var webService: WebService,
        private val currenciesDao: CurrenciesDao,
        private val exchangeRateDao: ExchangeRateDao
): CurrencyRepo {
    private var refreshCallback: ((CurrencyExchangeRate?) -> Unit)? = null
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun loadCurrencies(forceRemote: Boolean): List<Currency> =
        withContext(dispatcher){
            if(forceRemote)
                loadRemoteCurrencies() ?: arrayListOf()
            else {
                val cache = loadCachedCurrencies()
                 if(cache == null || cache.isNullOrEmpty())   {
                     loadRemoteCurrencies() ?: arrayListOf()
                 }else
                     cache
            }
        }

    override fun loadExchangeRates(
        callback: ((CurrencyExchangeRate?) -> Unit)?,
        forceRemote: Boolean
    ) {
        GlobalScope.launch(dispatcher) {
            if(forceRemote) {
                loadRemoteExchangeRates(callback)
            }else{
                val data = loadCachedExchangeRates()
                if(data == null)
                    loadRemoteExchangeRates(callback)
                else
                    callback?.invoke(data)
            }
        }
    }

    override fun registerExchangeRatesCallback(callback: (CurrencyExchangeRate?) -> Unit) {
        refreshCallback = callback
    }

/*   fun initExchangeRates(forceRemote: Boolean, () -> ) {
        GlobalScope.launch(dispatcher) {
            try{
                if(forceRemote)
                    exchangeRates.value = Data.success(loadRemoteExchangeRates())
                else {
                    val data = loadCachedExchangeRates() ?: loadRemoteExchangeRates()
                    exchangeRates.value = if(data != null) Data.success(data)  else Data.error(
                }
            }catch (ex: Exception){
                val msg = ex.message ?: "An error occurred while loading exchange rates"
                exchangeRates.value = Data.error(exchangeRates.value?.data, msg)
            }
        }
    }*/


    suspend fun loadCachedExchangeRates(): CurrencyExchangeRate? =
        withContext(dispatcher){
            exchangeRateDao.load()
        }

    suspend fun loadRemoteExchangeRates( callback: ((CurrencyExchangeRate?) -> Unit)?){
        webService.getExchangeRates().enqueue(object : Callback<CurrencyExchangeRate?> {
            override fun onFailure(call: Call<CurrencyExchangeRate?>, t: Throwable) {
                throw CurrencyException(t.message)
            }

            override fun onResponse(
                call: Call<CurrencyExchangeRate?>,
                response: Response<CurrencyExchangeRate?>
            ) {
                val body = response.body()
                if(body != null){
                    GlobalScope.launch { cacheExchangeRates(body) }
                }
                refreshCallback?.invoke(body)
                callback?.invoke(body)
            }
        })
    }
//    suspend fun loadRemoteExchangeRates(): CurrencyExchangeRate? =
//        withContext(dispatcher){
//            try{
//                val remote = webService.getExchangeRates().execute().body()
//                remote?.let { cacheExchangeRates(it) }
//                remote
//            }catch (ex: Exception){
//                throw CurrencyException(ex.message)
//            }
//        }

    private suspend fun cacheExchangeRates(data: CurrencyExchangeRate) {
        withContext(dispatcher){
            exchangeRateDao.clear()
            exchangeRateDao.save(data)
        }
    }

    suspend fun loadCachedCurrencies(): List<Currency>? =
        withContext(dispatcher){
            currenciesDao.load()
        }

    suspend fun loadRemoteCurrencies(): List<Currency>? =
        withContext(dispatcher) {
            try {
                val resp = webService.getCurrencies().execute().body() ?: return@withContext null
                val list = convertCurrencyMapToList(resp.currencies)
                cacheCurrencies(list)
                return@withContext list
            } catch (ex: Exception) {
                throw CurrencyException(ex.message)
            }
        }

    private suspend fun cacheCurrencies(currencies: List<Currency>) {
        withContext(dispatcher){
            currenciesDao.clear()
            currenciesDao.save(currencies)
        }
    }

    @VisibleForTesting
    fun setWebService(newWebService: WebService){
        webService = newWebService
    }
}