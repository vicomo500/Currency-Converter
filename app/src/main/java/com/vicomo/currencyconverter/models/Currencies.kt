package com.vicomo.currencyconverter.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vicomo.currencyconverter.persistence.CurrenciesMapTypeConverter

@Entity(tableName = "currencies")
data class Currencies (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val success: Boolean,
    val terms: String,
    val privacy: String,
    @TypeConverters(CurrenciesMapTypeConverter::class)
    val currencies: Map<String, String>
)