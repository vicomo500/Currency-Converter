package com.vicomo.currencyconverter.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vicomo.currencyconverter.MainApp
import com.vicomo.currencyconverter.repos.CurrencyRepo
import javax.inject.Inject

class ExchangeRatesWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    @Inject lateinit var repo: CurrencyRepo

    override fun doWork(): Result {
        val daggerAppComponent = (applicationContext as MainApp).daggerComponent()
        daggerAppComponent.inject(this)
        if(!::repo.isInitialized) return Result.retry()
        return try {
                repo.loadExchangeRates(null,true)
                    Result.success()
                }catch (throwable: Throwable){
                    Result.failure()
                }
    }
}
