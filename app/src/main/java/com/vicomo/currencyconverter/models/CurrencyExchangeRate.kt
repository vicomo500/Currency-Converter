package com.vicomo.currencyconverter.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vicomo.currencyconverter.persistence.ExchangeRatesMapTypeConverter

@Entity(tableName = "exchange_rates")
data class CurrencyExchangeRate (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val timestamp: String,
    val source: String,
    @TypeConverters(ExchangeRatesMapTypeConverter::class)
    val quotes: Map<String, Double>
)