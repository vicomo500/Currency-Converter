package com.vicomo.currencyconverter.persistence

import androidx.room.*
import com.vicomo.currencyconverter.models.Currency

@Dao
interface CurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(currencies: List<Currency>)

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun update(currency: Currency)
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun update(currencies: List<Currency>)

    @Query("SELECT * FROM currencies")
    fun load(): List<Currency>?

    @Query("DELETE FROM currencies")
    fun clear(): Int
}