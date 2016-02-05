package co.netguru.android.coolcal.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.concurrent.TimeUnit

data class ForecastResponse(@Expose @SerializedName("city") var city: City?,
                            @Expose @SerializedName("cnt") var cnt: Long?,
                            @Expose @SerializedName("list") var forecastList: List<Forecast> = ArrayList())

data class WeatherResponse(@Expose @SerializedName("coord") var coord: Coord?,
                           @Expose @SerializedName("weather") var weather: List<Weather> = ArrayList(),
                           @Expose @SerializedName("base") var base: String?,
                           @Expose @SerializedName("main") var main: Main?,
                           @Expose @SerializedName("clouds") var clouds: Clouds?,
                           @Expose @SerializedName("wind") var wind: Wind?,
                           @Expose @SerializedName("rain") var rain: Rain?,
                           @Expose @SerializedName("snow") var snow: Snow?,
                           @Expose @SerializedName("dt") var utcTime: Long?,
                           @Expose @SerializedName("sys") var sys: Sys?,
                           @Expose @SerializedName("name") var cityName: String?)

data class Forecast(@Expose @SerializedName("dt") var dt: Long?,
                    @Expose @SerializedName("main") var main: Main?,
                    @Expose @SerializedName("weather") var weatherList: List<Weather> = ArrayList(),
                    @Expose @SerializedName("clouds") var clouds: Clouds?,
                    @Expose @SerializedName("wind") var wind: Wind?,
                    @Expose @SerializedName("rain") var rain: Rain?,
                    @Expose @SerializedName("snow") var snow: Snow?,
                    @Expose @SerializedName("dt_txt") var dateString: String?) {

    fun dtStartMillis(): Long = dt!!.times(1000)
    fun dtStopMillis(): Long = dt!!.times(1000) + (MILLIS_3H) - 1
    fun range3h(): LongRange = dtStartMillis().rangeTo(dtStopMillis())

    companion object {
        val MILLIS_3H = TimeUnit.HOURS.toMillis(3)
    }
}


data class Main(@Expose @SerializedName("temp") var temperature: Double?,
                @Expose @SerializedName("temp_min") var tempMin: Double?,
                @Expose @SerializedName("temp_max") var tempMax: Double?,
                @Expose @SerializedName("pressure") var pressure: Double?,
                @Expose @SerializedName("humidity") var humidity: Long?)

data class Weather(@Expose @SerializedName("id") var id: Long?,
                   @Expose @SerializedName("main") var main: String?,
                   @Expose @SerializedName("description") var description: String?,
                   @Expose @SerializedName("icon") var icon: String?)

data class City(@Expose @SerializedName("id") var id: Long?,
                @Expose @SerializedName("name") var name: String?,
                @Expose @SerializedName("coord") var coord: Coord?,
                @Expose @SerializedName("country") var country: String?)

data class Coord(@Expose @SerializedName("lon") var longitude: Double?,
                 @Expose @SerializedName("lat") var latitude: Double?)

data class Wind(@Expose @SerializedName("speed") var speed: Double?,
                @Expose @SerializedName("deg") var deg: Double?)

data class Snow(@Expose @SerializedName("3h") var volume: Double?)

data class Rain(@Expose @SerializedName("3h") var precipitation: Double?)

data class Clouds(@Expose @SerializedName("all") var cloudiness: Long?)

data class Sys(@Expose @SerializedName("sunrise") var sunriseUtc: Long?,
               @Expose @SerializedName("sunset") var sunsetUtc: Long?)
