package co.netguru.android.coolcal.rendering

import android.content.res.Resources
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.weather.Temperature.celsius

class WeatherDescriptionHelper(val resources: Resources) {

    companion object {

        const val HIGH_PRESSURE = 1013f
        const val EXTREME_COLD = Int.MIN_VALUE
        const val EXTREME_COLD_END = -5
        const val FREEZING = -4
        const val FREEZING_END = 0
        const val COLD = 1
        const val COLD_END = 8
        const val MILD = 9
        const val MILD_END = 16
        const val WARM = 17
        const val WARM_END = 24
        const val HOT = 25
        const val HOT_END = 32
        const val HEAT = 33
        const val HEAT_END = Int.MAX_VALUE

    }

    fun getDescription(type: String?, temperature: Double?, pressure: Double?): String {

        if (pressure != null && temperature != null && type != null) {
            if (pressure >= HIGH_PRESSURE) {
                return getDescriptionForHighPressure(type, temperature)
            } else {
                return getDescriptionForLowPressure(type, temperature)
            }
        }
        return defaultMessage()
    }

    private fun getDescriptionForLowPressure(type: String, temperatureKelvin: Double): String {
        when (temperatureKelvin.celsius()) {
            in EXTREME_COLD..EXTREME_COLD_END -> return getExtremeColdLowDescription(type)
            in FREEZING..FREEZING_END -> return getFreezingLowDescription(type)
            in COLD..COLD_END -> return getColdLowDescription(type)
            in MILD..MILD_END -> return getMildLowDescription(type)
            in WARM..WARM_END -> return getWarmLowDescription(type)
            in HOT..HOT_END -> return getHotLowDescription(type)
            in HEAT..HEAT_END -> return getHeatLowDescription(type)
            else -> return defaultMessage()
        }
    }

    private fun getDescriptionForHighPressure(type: String, temperatureKelvin: Double): String {
        when (temperatureKelvin.celsius()) {
            in EXTREME_COLD..EXTREME_COLD_END -> return getExtremeColdHighDescription(type)
            in FREEZING..FREEZING_END -> return getFreezingHighDescription(type)
            in COLD..COLD_END -> return getColdHighDescription(type)
            in MILD..MILD_END -> return getMildHighDescription(type)
            in WARM..WARM_END -> return getWarmHighDescription(type)
            in HOT..HOT_END -> return getHotHighDescription(type)
            in HEAT..HEAT_END -> return getHeatHighDescription(type)
            else -> return defaultMessage()
        }
    }

    private fun getHeatHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_7_h)
    }

    private fun getHotHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_6_h)
    }

    private fun getWarmHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_5_h)
    }

    private fun getMildHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_4_h)
    }

    private fun getColdHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_3_h)
    }

    private fun getFreezingHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_2_h)
    }

    private fun getExtremeColdHighDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_1_h)
    }

    private fun getHeatLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_7_l)
    }

    private fun getHotLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_6_l)
    }

    private fun getWarmLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_5_l)
    }

    private fun getMildLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_4_l)
    }

    private fun getColdLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_3_l)
    }

    private fun getFreezingLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_2_l)
    }

    private fun getExtremeColdLowDescription(type: String): String {
        return getWeatherForType(type, R.array.weather_desc_array_1_l)
    }

    private fun getWeatherForType(type: String, weatherDescArrayId: Int): String {
        val descArray = resources.getStringArray(weatherDescArrayId)
        if (descArray.size != 18) {
            throw IllegalArgumentException("Wrong weather description size: ${descArray.size} should be 18")
        }
        return when (type) {
            WeatherCodes.CLEAR_SKY_DAY -> descArray[0]
            WeatherCodes.CLEAR_SKY_NIGHT -> descArray[1]
            WeatherCodes.FEW_CLOUDS_DAY -> descArray[2]
            WeatherCodes.FEW_CLOUDS_NIGHT -> descArray[3]
            WeatherCodes.SCATTERED_CLOUDS_DAY -> descArray[4]
            WeatherCodes.SCATTERED_CLOUDS_NIGHT -> descArray[5]
            WeatherCodes.BROKEN_CLOUDS_DAY -> descArray[6]
            WeatherCodes.BROKEN_CLOUDS_NIGHT -> descArray[7]
            WeatherCodes.SHOWER_RAIN_DAY -> descArray[8]
            WeatherCodes.SHOWER_RAIN_NIGHT -> descArray[9]
            WeatherCodes.RAIN_DAY -> descArray[10]
            WeatherCodes.RAIN_NIGHT -> descArray[11]
            WeatherCodes.THUNDERSTORM_DAY -> descArray[12]
            WeatherCodes.THUNDERSTORM_NIGHT -> descArray[13]
            WeatherCodes.SNOW_DAY -> descArray[14]
            WeatherCodes.SNOW_NIGHT -> descArray[15]
            WeatherCodes.MIST_DAY -> descArray[16]
            WeatherCodes.MIST_NIGHT -> descArray[17]
            else -> defaultMessage()
        }
    }

    private fun defaultMessage(): String {
        return resources.getString(R.string.weather_desc_default)
    }

}