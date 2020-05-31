package com.vicomo.currencyconverter

import com.vicomo.currencyconverter.dagger.DaggerTestAppComponent
import com.vicomo.currencyconverter.dagger.TestAppComponent

class TestApp: MainApp() {
    private lateinit var daggerComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        daggerComponent = DaggerTestAppComponent.builder()
            .bindContext(this)
            .build()
    }

     override fun daggerComponent(): TestAppComponent = daggerComponent
}