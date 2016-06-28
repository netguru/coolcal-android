package co.netguru.android.coolcal.rendering

import co.netguru.android.coolcal.R

class WeatherDecoder {


    fun getIconRes(code: String?): Int {
        return when (code) {
            WeatherCodes.CLEAR_SKY_DAY -> R.drawable.icon_clear_sky_day
            WeatherCodes.CLEAR_SKY_NIGHT -> R.drawable.icon_clear_sky_night
            WeatherCodes.FEW_CLOUDS_DAY -> R.drawable.icon_few_clouds_day
            WeatherCodes.FEW_CLOUDS_NIGHT -> R.drawable.icon_few_clouds_night
            WeatherCodes.SCATTERED_CLOUDS_DAY -> R.drawable.icon_scattered_clouds_day
            WeatherCodes.SCATTERED_CLOUDS_NIGHT -> R.drawable.icon_scattered_clouds_night
            WeatherCodes.BROKEN_CLOUDS_DAY -> R.drawable.icon_broken_clouds_day
            WeatherCodes.BROKEN_CLOUDS_NIGHT -> R.drawable.icon_broken_clouds_night
            WeatherCodes.SHOWER_RAIN_DAY -> R.drawable.icon_shower_rain_day
            WeatherCodes.SHOWER_RAIN_NIGHT -> R.drawable.icon_shower_rain_night
            WeatherCodes.RAIN_DAY -> R.drawable.icon_rain_day
            WeatherCodes.RAIN_NIGHT -> R.drawable.icon_rain_night
            WeatherCodes.THUNDERSTORM_DAY -> R.drawable.icon_thunderstorm_day
            WeatherCodes.THUNDERSTORM_NIGHT -> R.drawable.icon_thunderstorm_night
            WeatherCodes.SNOW_DAY -> R.drawable.icon_snow_day
            WeatherCodes.SNOW_NIGHT -> R.drawable.icon_snow_night
            WeatherCodes.MIST_DAY -> R.drawable.icon_mist_day
            WeatherCodes.MIST_NIGHT -> R.drawable.icon_mist_night
            else -> R.drawable.icon_clear_sky_day // shouldn't happen anyways
        }
    }

    fun getBackgroundsRes(code: String?) = when (code) {
        WeatherCodes.CLEAR_SKY_DAY -> R.drawable.illustration_clear_sky_day
        WeatherCodes.CLEAR_SKY_NIGHT -> R.drawable.illustration_clear_sky_night
        WeatherCodes.FEW_CLOUDS_DAY -> R.drawable.illustration_few_clouds_day
        WeatherCodes.FEW_CLOUDS_NIGHT -> R.drawable.illustration_few_clouds_night
        WeatherCodes.SCATTERED_CLOUDS_DAY -> R.drawable.illustration_scattered_clouds_day
        WeatherCodes.SCATTERED_CLOUDS_NIGHT -> R.drawable.illustration_scattered_clouds_night
        WeatherCodes.BROKEN_CLOUDS_DAY -> R.drawable.illustration_broken_clouds_day
        WeatherCodes.BROKEN_CLOUDS_NIGHT -> R.drawable.illustration_broken_clouds_night
        WeatherCodes.SHOWER_RAIN_DAY -> R.drawable.illustration_shower_rain_day
        WeatherCodes.SHOWER_RAIN_NIGHT -> R.drawable.illustration_shower_rain_night
        WeatherCodes.RAIN_DAY -> R.drawable.illustration_rain_day
        WeatherCodes.RAIN_NIGHT -> R.drawable.illustration_rain_night
        WeatherCodes.THUNDERSTORM_DAY -> R.drawable.illustration_thunderstorm_day
        WeatherCodes.THUNDERSTORM_NIGHT -> R.drawable.illustration_thunderstorm_night
        WeatherCodes.SNOW_DAY -> R.drawable.illustration_snow_day
        WeatherCodes.SNOW_NIGHT -> R.drawable.illustration_snow_night
        WeatherCodes.MIST_DAY -> R.drawable.illustration_mist_day
        WeatherCodes.MIST_NIGHT -> R.drawable.illustration_mist_night
        else -> R.drawable.illustration_clear_sky_day
    }
}