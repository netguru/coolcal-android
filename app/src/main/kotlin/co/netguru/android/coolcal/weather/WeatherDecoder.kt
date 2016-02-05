package co.netguru.android.coolcal.weather

import co.netguru.android.coolcal.R

object WeatherDecoder {

    fun getIconRes(code: String?) = when (code) {
        "01d" -> R.drawable.icon_clear_sky_day
        "01n" -> R.drawable.icon_clear_sky_night
        "02d" -> R.drawable.icon_few_clouds_day
        "02n" -> R.drawable.icon_few_clouds_night
        "03d" -> R.drawable.icon_scattered_clouds_day
        "03n" -> R.drawable.icon_scattered_clouds_night
        "04d" -> R.drawable.icon_broken_clouds_day
        "04n" -> R.drawable.icon_broken_clouds_night
        "09d" -> R.drawable.icon_shower_rain_day
        "09n" -> R.drawable.icon_shower_rain_night
        "10d" -> R.drawable.icon_rain_day
        "10n" -> R.drawable.icon_rain_night
        "11d" -> R.drawable.icon_thunderstorm_day
        "11n" -> R.drawable.icon_thunderstorm_night
        "13d" -> R.drawable.icon_snow_day
        "13n" -> R.drawable.icon_snow_night
        "50d" -> R.drawable.icon_mist_day
        "50n" -> R.drawable.icon_mist_night
        else -> R.drawable.icon_clear_sky_day // todo: separate default?
    }
}