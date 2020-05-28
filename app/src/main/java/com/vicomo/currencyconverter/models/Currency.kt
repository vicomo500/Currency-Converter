package com.vicomo.currencyconverter.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "currencies",
    indices = [Index( value = ["code", "name"], unique = true)]
)
data class Currency (
    @NonNull
    var code: String,
    @NonNull
    var name: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)