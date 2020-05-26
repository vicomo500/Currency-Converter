package com.vicomo.currencyconverter.dagger

import android.content.Context
import com.vicomo.currencyconverter.BuildConfig
import com.vicomo.currencyconverter.persistence.AppDatabase
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.vicomo.currencyconverter.remote.BASE_URL
import com.vicomo.currencyconverter.remote.WebService
import com.vicomo.currencyconverter.repos.CurrencyRepoImpl
import com.vicomo.currencyconverter.repos.CurrencyRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepoModule {

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideWebService(): WebService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(
                    OkHttpClient.Builder().addInterceptor { chain ->
                        val url = chain
                            .request()
                            .url()
                            .newBuilder()
                            .addQueryParameter("access_key", BuildConfig.API_KEY)
                            .build()
                        chain.proceed(chain.request().newBuilder().url(url).build())
                    }.build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WebService::class.java)
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideExchangeRateDao(context: Context) = AppDatabase.getInstance(context).exchangeRateDao()

        @Singleton
        @JvmStatic
        @Provides
        fun provideCurrenciesDao(context: Context) = AppDatabase.getInstance(context).currenciesDao()
    }

    @Binds
    abstract fun bindWeatherRepo(repoImpl: CurrencyRepoImpl): CurrencyRepo

}