package co.netguru.android.coolcal.rendering

import android.content.res.Resources
import co.netguru.android.coolcal.R

class WeatherDescriptionHelper(val resources: Resources) {

    companion object {

        const val HIGH_PRESSURE = 1013f
        const val MIN_TEMP = Int.MIN_VALUE
        const val FREEZING = -4
        const val COLD = 1
        const val MILD = 9
        const val WARM = 17
        const val HOT = 25
        const val HEAT = 33
        const val MAX_TEMP = Int.MAX_VALUE

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

    private fun getDescriptionForLowPressure(type: String, temperature: Double): String {
        when (convertToCelsius(temperature)) {
            in MIN_TEMP..FREEZING -> return getFreezingLowDescription(type)
            in FREEZING..COLD -> return getColdLowDescription(type)
            in COLD..MILD -> return getMildLowDescription(type)
            in WARM..HOT -> return getWarmLowDescription(type)
            in HOT..HEAT -> return getHotLowDescription(type)
            in HEAT..MAX_TEMP -> return getHeatLowDescription(type)
            else -> return  defaultMessage()
        }
    }

    private fun getDescriptionForHighPressure(type: String, temperature: Double): String {
        when (convertToCelsius(temperature)) {
            in MIN_TEMP..FREEZING -> return getFreezingHighDescription(type)
            in FREEZING..COLD -> return getColdHighDescription(type)
            in COLD..MILD -> return getMildHighDescription(type)
            in WARM..HOT -> return getWarmHighDescription(type)
            in HOT..HEAT -> return getHotHighDescription(type)
            in HEAT..MAX_TEMP -> return getHeatHighDescription(type)
            else -> return  defaultMessage()
        }
    }

    private fun convertToCelsius(temperature: Double): Double {
        return temperature - 273.15
    }

    private fun getHeatHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getHotHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getWarmHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getMildHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getColdHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getFreezingHighDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getHeatLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getHotLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getWarmLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getMildLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getColdLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getFreezingLowDescription(type: String): String {
        return getWeatherForType(type,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example,
                R.string.weather_desc_example)
    }

    private fun getWeatherForType(type: String, clearSkyD: Int, clearSkyN: Int, FewCloudsD: Int, FewCloudsN: Int, scatteredCloudsD: Int,
                                  scatteredCloudsN: Int, brokenCloudsD: Int, brokenCloudsN: Int, showerRainD: Int, showerRainN: Int,
                                  rainD: Int, rainN: Int, thunderstormD: Int, thunderstormN: Int, snowD: Int,
                                  snowN: Int, mistD: Int, mistN: Int): String {
        return when (type) {
            WeatherCodes.CLEAR_SKY_DAY -> getDescription(clearSkyD)
            WeatherCodes.CLEAR_SKY_NIGHT -> getDescription(clearSkyN)
            WeatherCodes.FEW_CLOUDS_DAY -> getDescription(FewCloudsD)
            WeatherCodes.FEW_CLOUDS_NIGHT -> getDescription(FewCloudsN)
            WeatherCodes.SCATTERED_CLOUDS_DAY -> getDescription(scatteredCloudsD)
            WeatherCodes.SCATTERED_CLOUDS_NIGHT -> getDescription(scatteredCloudsN)
            WeatherCodes.BROKEN_CLOUDS_DAY -> getDescription(brokenCloudsD)
            WeatherCodes.BROKEN_CLOUDS_NIGHT -> getDescription(brokenCloudsN)
            WeatherCodes.SHOWER_RAIN_DAY -> getDescription(showerRainD)
            WeatherCodes.SHOWER_RAIN_NIGHT -> getDescription(showerRainN)
            WeatherCodes.RAIN_DAY -> getDescription(rainD)
            WeatherCodes.RAIN_NIGHT -> getDescription(rainN)
            WeatherCodes.THUNDERSTORM_DAY -> getDescription(thunderstormD)
            WeatherCodes.THUNDERSTORM_NIGHT -> getDescription(thunderstormN)
            WeatherCodes.SNOW_DAY -> getDescription(snowD)
            WeatherCodes.SNOW_NIGHT -> getDescription(snowN)
            WeatherCodes.MIST_DAY -> getDescription(mistD)
            WeatherCodes.MIST_NIGHT -> getDescription(mistN)
            else -> defaultMessage()
        }
    }

    private fun getDescription(id: Int): String{
        return resources.getString(id)
    }

    private fun defaultMessage(): String {
        return resources.getString(R.string.weather_desc_default)
    }

}