package com.vicomo.currencyconverter.viewmodels

import androidx.lifecycle.ViewModel
import com.vicomo.currencyconverter.repos.CurrencyRepo
import javax.inject.Inject

class MainActivityViewModel
@Inject constructor(
    private val repo: CurrencyRepo
): ViewModel() {

}