package com.vicomo.currencyconverter.views

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.vicomo.currencyconverter.MainApp
import com.vicomo.currencyconverter.R
import com.vicomo.currencyconverter.models.Currency
import com.vicomo.currencyconverter.models.CurrencyAmount
import com.vicomo.currencyconverter.utils.EspressoIdlingResource
import com.vicomo.currencyconverter.utils.ExchangeRatesWorker
import com.vicomo.currencyconverter.utils.Status
import com.vicomo.currencyconverter.viewmodels.MainActivityViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var workerManager: WorkManager
    private lateinit var workRequest: PeriodicWorkRequest
    //UI ELEMENTS
    private lateinit var edTxt : EditText
    private lateinit var spn : Spinner
    private lateinit var btn : Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvFeedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialize dagger for view model
        val daggerComponent =  (application as MainApp).daggerComponent()
        daggerComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        workerManager = WorkManager.getInstance(this)
        initUI()
        initObservers()
        initRefreshWorker()
    }

    private fun initUI(){
        edTxt = findViewById(R.id.edtxt_amt)
        spn = findViewById(R.id.spn_currencies)
        btn = findViewById(R.id.btn_submit)
        recyclerView = findViewById(R.id.recyclerView)
        tvFeedback = findViewById(R.id.tv_feedback)
        btn.setOnClickListener {
            try{
                val item = spn.selectedItem as Currency
                val amt = edTxt.text.toString().toDouble()
                viewModel.calculate(amt, item)
            }catch (ex: Exception){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initObservers(){
        observeData()
        observeCurrencies()
        observeExchangeRates()
    }

    private fun observeData(){
        //observe for Data
        viewModel.data.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    enableControls(true)
                    stopProgress()
                    if(it.data == null || it.data.isEmpty()){
                        showFeedBack("Empty Data")
                        return@Observer
                    }
                    showData(it.data)
                    EspressoIdlingResource.decrement()
                }

                Status.LOADING -> {
                    enableControls(false)
                    showProgress()
                    EspressoIdlingResource.increment()
                }
                Status.ERROR -> {
                    enableControls(true)
                    stopProgress()
                    showFeedBack(it.message ?: "an error occurred")
                    EspressoIdlingResource.decrement()
                }
            }
            EspressoIdlingResource.decrement()
        })
    }

    private fun showProgress() {
        btn.text = getString(R.string.progress)
    }

    private fun stopProgress(){
        btn.text = getString(R.string.calculate)
    }

    private fun observeCurrencies(){
        //observe for Currencies
        viewModel.currencies.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    if(it.data == null || it.data.isEmpty()) return@Observer
                    spn.adapter = CurrencyAdapter(this, ArrayList(it.data))
                }

                Status.LOADING -> {
                    Toast.makeText(this, "loading currencies", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeExchangeRates(){
        //observe for ExchangeRates
        viewModel.exchangeRates.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(this, "exchange rates refreshed", Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    Toast.makeText(this, "refreshing exchange rates", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun enableControls(enable: Boolean){
        edTxt.isEnabled = enable
        spn.isEnabled = enable
        btn.isEnabled = enable
    }

    private fun showFeedBack(msg: String = ""){
        tvFeedback.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        if(msg.isNotEmpty())
            tvFeedback.text = msg
    }

    private fun showData(data: List<CurrencyAmount>){
        recyclerView.visibility = View.VISIBLE
        tvFeedback.visibility = View.GONE
        tvFeedback.text = null
        if(recyclerView.adapter == null)
            recyclerView.adapter = RatesAdapter(this, ArrayList(data))
        else
            (recyclerView.adapter as RatesAdapter).update(data)
    }

    private fun initRefreshWorker(){
        workRequest =
            PeriodicWorkRequest.Builder(ExchangeRatesWorker::class.java, 30, TimeUnit.MINUTES)
                .addTag(ExchangeRatesWorker::class.java.simpleName)
                .build()
        workerManager.enqueueUniquePeriodicWork(
            ExchangeRatesWorker::class.java.simpleName,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onStop() {
        if(::workerManager.isInitialized && ::workRequest.isInitialized)
            workerManager.cancelAllWorkByTag(ExchangeRatesWorker::class.java.simpleName)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if(::workerManager.isInitialized && ::workRequest.isInitialized)
            workerManager.enqueue(workRequest)
    }

    @VisibleForTesting
    fun viewModel() = viewModel
}
