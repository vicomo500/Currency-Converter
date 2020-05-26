package com.vicomo.currencyconverter.persistence

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.vicomo.currencyconverter.models.Currencies
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrenciesDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CurrenciesDao
    private lateinit var testObject: Currencies

    @Before
    fun setUp() {
        //database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        //dao
        dao = database.currenciesDao()
        //test object
        testObject = Currencies(
            1,
            true,
            "https://currencylayer.com/terms",
            "https://currencylayer.com/privacy",
            mapOf(
                Pair("USD", "United States Dollar"),
                Pair("GBP", "British Pound Sterling"),
                Pair("EUR", "Euro"),
                Pair("JPY", "Japanese Yen"),
                Pair("NGN", "Nigerian Naira")
            )
        )
    }

    @After
    fun cleanUp() = database.close()

    @Test
    fun data_isSaved(){
        // GIVEN - live exchange rates is cached
        dao.save(testObject)
        // WHEN - load cached data
        val currencies = dao.load()
        // THEN - contains the expected values
        assertThat(currencies != null, `is`(true))
        assertThat(currencies!!.id, `is`(testObject.id))
        assertThat(currencies.success, `is`(testObject.success))
        assertThat(currencies.terms, `is`(testObject.terms))
        assertThat(currencies.privacy, `is`(testObject.privacy))
        assertThat(currencies.currencies.size, `is`(testObject.currencies.size))
        assertThat(currencies.currencies["USD"], `is`(testObject.currencies["USD"]))
        assertThat(currencies.currencies["GBY"], `is`(testObject.currencies["GBY"]))
    }

    @Test
    fun clearDB(){
        // GIVEN - live exchange rates is cached
        dao.save(testObject)
        // WHEN - clear cached data
        dao.clear()
        // THEN - no data in DB
        val currencies = dao.load()
        assertThat(currencies == null, `is`(true))
    }
}