# Currency Converter
A simple android application that allows a user view exchange rates for any given currency.

## Screenshots
<img src="screen-shots/home-screen.png" height="400" alt="UI wireframe"/> 

### Functional Requirements:
- [ ] Exchange rates must be fetched from: [https://currencylayer.com/documentation](https://currencylayer.com/documentation)  
- [ ] Use free API Access Key for using the API
- [ ] User must be able to select a currency from a list of currencies provided by the API (for currencies that are not available, convert them on the app side. When converting, floating-point error is acceptable)
- [ ] User must be able to enter desired amount for selected currency
- [ ] User should then see a list of exchange rates for the selected currency
- [ ] Rates should be persisted locally and refreshed no more frequently than every 30 minutes (to limit bandwidth usage)
-----------
Libraries
---------
 * Android Jetpack i.e. ViewModel, LiveData, Room & WorkerManager
 * Kotlin coroutines
 * Dagger - https://github.com/google/dagger
 * Retrofit - http://square.github.io/retrofit
-------------
NOTE: To run this project
-------
* > register and get your api key at [https://currencylayer.com/product](https://currencylayer.com/product)
* > create a secret.properties file in the project root directory
* > put your api key in format **API_KEY="XXXXXXXXXXXXXXX"**

download the apk file here [link](app/release/app-release.apk).