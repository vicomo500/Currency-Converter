package com.vicomo.currencyconverter.dagger

import com.vicomo.currencyconverter.fakes.FakeCurrencyRepo
import com.vicomo.currencyconverter.repos.CurrencyRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestRepoModule {

    @Singleton
    @Binds
    abstract fun bindWeatherRepo(repoImpl: FakeCurrencyRepo): CurrencyRepo

}