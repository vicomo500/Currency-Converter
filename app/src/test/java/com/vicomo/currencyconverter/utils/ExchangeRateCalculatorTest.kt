package com.vicomo.currencyconverter.utils


import com.vicomo.currencyconverter.fakes.cachedCurrencies
import com.vicomo.currencyconverter.models.CurrencyAmount
import com.vicomo.currencyconverter.models.CurrencyExchangeRate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.Rule

class ExchangeRateCalculatorTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var  ratesUSD : CurrencyExchangeRate
    private lateinit var  ratesJPY : CurrencyExchangeRate
    private lateinit var  expected_10_GBP : List<CurrencyAmount>
    private lateinit var  expected_10_JPY : List<CurrencyAmount>
    private val amt = 10.0
    private val pounds = cachedCurrencies[1]
    private val yen = cachedCurrencies[3]

    @ExperimentalCoroutinesApi
    @Test
    fun calculate_generatesValidExchangeRates() = runBlockingTest {
        //GIVEN: calculator with exchange rates in japanese yen
        val calculator = ExchangeRateCalculator(ratesJPY, cachedCurrencies, Dispatchers.Unconfined)
        //WHEN: calculate for £10
        calculator.calculate(amt,pounds){
            //before testing, round up floating point numbers to one decimal place to avoid precision errors
            it.forEach{ amt -> amt.amount = amt.amount.round (1) }
            expected_10_GBP.forEach { amt -> amt.amount = amt.amount.round (1)}
            assertThat(it).containsAnyIn(expected_10_GBP)
            assertThat(it).containsAnyOf(expected_10_GBP[0], expected_10_GBP[2], expected_10_GBP[3])
        }
        //WHEN: calculate for ¥10
        calculator.calculate(amt,yen){
            it.forEach{ amt -> amt.amount = amt.amount.round (1) }
            expected_10_JPY.forEach { amt -> amt.amount = amt.amount.round (1)}
            assertThat(it).containsAnyIn(expected_10_JPY)
            assertThat(it).containsAnyOf(expected_10_JPY[0], expected_10_JPY[1], expected_10_JPY[2])
        }
        //WHEN: calculate for ¥0
        calculator.calculate(0.0,yen){
            it.forEach { amt ->
                assertThat(amt.amount).isEqualTo(0.0)
            }
        }
    }

    @Before
    fun setUp(){
        ratesUSD = CurrencyExchangeRate(
            1,
            "111111111",
            "USD",
            mapOf(
                Pair("USD", 1.0),
                Pair("GBP", 0.82),
                Pair("EUR", 0.91),
                Pair("JPY", 107.77)
            )
        )
        ratesJPY = CurrencyExchangeRate(
            2,
            "111111111",
            "JPY",
            mapOf(
                Pair("USD", 0.0093),
                Pair("GBP", 0.0076),
                Pair("EUR", 0.0084),
                Pair("JPY", 1.0)
            )
        )
        expected_10_GBP = listOf(
            CurrencyAmount(12.1951219512195, cachedCurrencies[0]),//USD
            CurrencyAmount(amt, cachedCurrencies[1]),//GBP
            CurrencyAmount(11.09756097560976, cachedCurrencies[2]),//euro
            CurrencyAmount(1314.2682926, cachedCurrencies[3])//yen
        )
        expected_10_JPY = listOf(
            CurrencyAmount(0.093, cachedCurrencies[0]),//USD
            CurrencyAmount(0.076, cachedCurrencies[1]),//GBP
            CurrencyAmount(0.084, cachedCurrencies[2]),//euro
            CurrencyAmount(amt, cachedCurrencies[3])//yen
        )
    }
}