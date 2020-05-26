package com.vicomo.currencyconverter.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vicomo.currencyconverter.models.Currencies

@Dao
interface CurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(currencies: Currencies)

    @Query("SELECT * FROM currencies LIMIT 1")
    fun load(): Currencies?

    @Query("DELETE FROM currencies")
    fun clear()
}