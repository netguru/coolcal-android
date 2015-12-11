package co.netguru.android.owm.api

import retrofit.http.GET

import retrofit.http.Query
import rx.Observable

/**
 * This interface specifies REST methods with route for Retrofit to generate proper implementation
 */
interface OpenWeatherMapApi {

    companion object {
        val WEATHER_API_ROOT = "http://api.openweathermap.org/data/2.5/"
    }

    /*
        Current weather
     */
    @GET("weather")
    fun getWeather(@Query("q") city: String): Observable<WeatherResponse>

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double,
                   @Query("lon") longitude: Double): Observable<WeatherResponse>

    /*
        5 day / 3h Forecast
     */
    @GET("forecast")
    fun getForecast(@Query("q") city: String,
                    @Query("cnt") count: Long = 0): Observable<ForecastResponse>

    @GET("forecast")
    fun getForecast(@Query("lat") latitude: Double,
                    @Query("lon") longitude: Double,
                    @Query("cnt") count: Long = 0): Observable<ForecastResponse>

}
/*
    Querying for cities - ids
 */
//    http://api.openweathermap.org/data/2.5/
//         find?q=<searchphrase>&type=like&sort=population&cnt=<howmany>&appid=<appid>
