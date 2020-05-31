package com.vicomo.currencyconverter

import android.app.Application
import com.vicomo.currencyconverter.dagger.AppComponent
import com.vicomo.currencyconverter.dagger.DaggerAppComponent

open class MainApp: Application() {
    private lateinit var daggerComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerAppComponent
            .builder()
            .bindContext(this)
            .build()
    }
    open fun daggerComponent(): AppComponent = daggerComponent
}