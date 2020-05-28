package com.vicomo.currencyconverter.fakes

import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.persistence.CurrenciesDao

class FakeCurrenciesDao(
    private val list: ArrayList<Currency>
): CurrenciesDao {

    override fun save(currency: Currency) {
        list.add(currency)
    }

    override fun save(currencies: List<Currency>) {
        list.addAll(currencies)
    }

    override fun load(): List<Currency>?  = list

    override fun clear(): Int {
        list.clear()
        return list.size
    }
}