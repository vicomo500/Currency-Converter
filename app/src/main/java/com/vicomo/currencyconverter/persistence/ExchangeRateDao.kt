package com.vicomo.currencyconverter.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vicomo.currencyconverter.models.CurrencyExchangeRate

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(exchangeRate: CurrencyExchangeRate)

    @Query("SELECT * FROM exchange_rates LIMIT 1")
    fun load(): CurrencyExchangeRate?

    @Query("DELETE FROM exchange_rates")
    fun clear()
}