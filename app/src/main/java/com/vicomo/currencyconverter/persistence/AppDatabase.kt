package com.vicomo.currencyconverter.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vicomo.currencyconverter.models.Currencies
import com.vicomo.currencyconverter.models.CurrencyExchangeRate

@Database(entities = [CurrencyExchangeRate::class, Currencies::class], version = 1, exportSchema = false)
@TypeConverters(value = [ExchangeRatesMapTypeConverter::class, CurrenciesMapTypeConverter::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun currenciesDao(): CurrenciesDao
    companion object {
        private const val DB_NAME = "currency_converter_db"
        fun getInstance(context: Context) : AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
    }
}