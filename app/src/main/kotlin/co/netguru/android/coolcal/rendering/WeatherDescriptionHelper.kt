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
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_7_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_7_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_7_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_7_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_7_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_7_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_7_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_7_h,
                showerRainD = R.string.weather_desc_shower_rain_d_7_h,
                showerRainN = R.string.weather_desc_shower_rain_n_7_h,
                rainD = R.string.weather_desc_rain_d_7_h,
                rainN = R.string.weather_desc_rain_n_7_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_7_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_7_h,
                snowD = R.string.weather_desc_snow_d_7_h,
                snowN = R.string.weather_desc_snow_n_7_h,
                mistD = R.string.weather_desc_mist_d_7_h,
                mistN = R.string.weather_desc_mist_n_7_h)
    }

    private fun getHotHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_6_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_6_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_6_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_6_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_6_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_6_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_6_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_6_h,
                showerRainD = R.string.weather_desc_shower_rain_d_6_h,
                showerRainN = R.string.weather_desc_shower_rain_n_6_h,
                rainD = R.string.weather_desc_rain_d_6_h,
                rainN = R.string.weather_desc_rain_n_6_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_6_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_6_h,
                snowD = R.string.weather_desc_snow_d_6_h,
                snowN = R.string.weather_desc_snow_n_6_h,
                mistD = R.string.weather_desc_mist_d_6_h,
                mistN = R.string.weather_desc_mist_n_6_h)
    }

    private fun getWarmHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_5_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_5_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_5_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_5_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_5_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_5_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_5_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_5_h,
                showerRainD = R.string.weather_desc_shower_rain_d_5_h,
                showerRainN = R.string.weather_desc_shower_rain_n_5_h,
                rainD = R.string.weather_desc_rain_d_5_h,
                rainN = R.string.weather_desc_rain_n_5_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_5_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_5_h,
                snowD = R.string.weather_desc_snow_d_5_h,
                snowN = R.string.weather_desc_snow_n_5_h,
                mistD = R.string.weather_desc_mist_d_5_h,
                mistN = R.string.weather_desc_mist_n_5_h)
    }

    private fun getMildHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_4_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_4_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_4_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_4_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_4_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_4_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_4_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_4_h,
                showerRainD = R.string.weather_desc_shower_rain_d_4_h,
                showerRainN = R.string.weather_desc_shower_rain_n_4_h,
                rainD = R.string.weather_desc_rain_d_4_h,
                rainN = R.string.weather_desc_rain_n_4_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_4_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_4_h,
                snowD = R.string.weather_desc_snow_d_4_h,
                snowN = R.string.weather_desc_snow_n_4_h,
                mistD = R.string.weather_desc_mist_d_4_h,
                mistN = R.string.weather_desc_mist_n_4_h)
    }

    private fun getColdHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_3_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_3_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_3_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_3_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_3_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_3_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_3_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_3_h,
                showerRainD = R.string.weather_desc_shower_rain_d_3_h,
                showerRainN = R.string.weather_desc_shower_rain_n_3_h,
                rainD = R.string.weather_desc_rain_d_3_h,
                rainN = R.string.weather_desc_rain_n_3_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_3_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_3_h,
                snowD = R.string.weather_desc_snow_d_3_h,
                snowN = R.string.weather_desc_snow_n_3_h,
                mistD = R.string.weather_desc_mist_d_3_h,
                mistN = R.string.weather_desc_mist_n_3_h)
    }

    private fun getFreezingHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_2_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_2_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_2_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_2_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_2_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_2_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_2_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_2_h,
                showerRainD = R.string.weather_desc_shower_rain_d_2_h,
                showerRainN = R.string.weather_desc_shower_rain_n_2_h,
                rainD = R.string.weather_desc_rain_d_2_h,
                rainN = R.string.weather_desc_rain_n_2_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_2_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_2_h,
                snowD = R.string.weather_desc_snow_d_2_h,
                snowN = R.string.weather_desc_snow_n_2_h,
                mistD = R.string.weather_desc_mist_d_2_h,
                mistN = R.string.weather_desc_mist_n_2_h)
    }

    private fun getExtremeColdHighDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_1_h,
                clearSkyN = R.string.weather_desc_clear_sky_n_1_h,
                FewCloudsD = R.string.weather_desc_few_clouds_d_1_h,
                FewCloudsN = R.string.weather_desc_few_clouds_n_1_h,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_1_h,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_1_h,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_1_h,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_1_h,
                showerRainD = R.string.weather_desc_shower_rain_d_1_h,
                showerRainN = R.string.weather_desc_shower_rain_n_1_h,
                rainD = R.string.weather_desc_rain_d_1_h,
                rainN = R.string.weather_desc_rain_n_1_h,
                thunderstormD = R.string.weather_desc_thunderstorm_d_1_h,
                thunderstormN = R.string.weather_desc_thunderstorm_n_1_h,
                snowD = R.string.weather_desc_snow_d_1_h,
                snowN = R.string.weather_desc_snow_n_1_h,
                mistD = R.string.weather_desc_mist_d_1_h,
                mistN = R.string.weather_desc_mist_n_1_h)
    }

    private fun getHeatLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_7_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_7_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_7_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_7_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_7_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_7_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_7_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_7_l,
                showerRainD = R.string.weather_desc_shower_rain_d_7_l,
                showerRainN = R.string.weather_desc_shower_rain_n_7_l,
                rainD = R.string.weather_desc_rain_d_7_l,
                rainN = R.string.weather_desc_rain_n_7_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_7_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_7_l,
                snowD = R.string.weather_desc_snow_d_7_l,
                snowN = R.string.weather_desc_snow_n_7_l,
                mistD = R.string.weather_desc_mist_d_7_l,
                mistN = R.string.weather_desc_mist_n_7_l)
    }

    private fun getHotLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_6_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_6_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_6_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_6_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_6_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_6_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_6_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_6_l,
                showerRainD = R.string.weather_desc_shower_rain_d_6_l,
                showerRainN = R.string.weather_desc_shower_rain_n_6_l,
                rainD = R.string.weather_desc_rain_d_6_l,
                rainN = R.string.weather_desc_rain_n_6_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_6_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_6_l,
                snowD = R.string.weather_desc_snow_d_6_l,
                snowN = R.string.weather_desc_snow_n_6_l,
                mistD = R.string.weather_desc_mist_d_6_l,
                mistN = R.string.weather_desc_mist_n_6_l)
    }

    private fun getWarmLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_5_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_5_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_5_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_5_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_5_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_5_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_5_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_5_l,
                showerRainD = R.string.weather_desc_shower_rain_d_5_l,
                showerRainN = R.string.weather_desc_shower_rain_n_5_l,
                rainD = R.string.weather_desc_rain_d_5_l,
                rainN = R.string.weather_desc_rain_n_5_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_5_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_5_l,
                snowD = R.string.weather_desc_snow_d_5_l,
                snowN = R.string.weather_desc_snow_n_5_l,
                mistD = R.string.weather_desc_mist_d_5_l,
                mistN = R.string.weather_desc_mist_n_5_l)
    }

    private fun getMildLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_4_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_4_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_4_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_4_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_4_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_4_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_4_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_4_l,
                showerRainD = R.string.weather_desc_shower_rain_d_4_l,
                showerRainN = R.string.weather_desc_shower_rain_n_4_l,
                rainD = R.string.weather_desc_rain_d_4_l,
                rainN = R.string.weather_desc_rain_n_4_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_4_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_4_l,
                snowD = R.string.weather_desc_snow_d_4_l,
                snowN = R.string.weather_desc_snow_n_4_l,
                mistD = R.string.weather_desc_mist_d_4_l,
                mistN = R.string.weather_desc_mist_n_4_l)
    }

    private fun getColdLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_3_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_3_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_3_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_3_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_3_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_3_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_3_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_3_l,
                showerRainD = R.string.weather_desc_shower_rain_d_3_l,
                showerRainN = R.string.weather_desc_shower_rain_n_3_l,
                rainD = R.string.weather_desc_rain_d_3_l,
                rainN = R.string.weather_desc_rain_n_3_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_3_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_3_l,
                snowD = R.string.weather_desc_snow_d_3_l,
                snowN = R.string.weather_desc_snow_n_3_l,
                mistD = R.string.weather_desc_mist_d_3_l,
                mistN = R.string.weather_desc_mist_n_3_l)
    }

    private fun getFreezingLowDescription(type: String): String {
        return getWeatherForType(type, clearSkyD = R.string.weather_desc_clear_sky_d_2_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_2_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_2_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_2_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_2_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_2_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_2_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_2_l,
                showerRainD = R.string.weather_desc_shower_rain_d_2_l,
                showerRainN = R.string.weather_desc_shower_rain_n_2_l,
                rainD = R.string.weather_desc_rain_d_2_l,
                rainN = R.string.weather_desc_rain_n_2_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_2_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_2_l,
                snowD = R.string.weather_desc_snow_d_2_l,
                snowN = R.string.weather_desc_snow_n_2_l,
                mistD = R.string.weather_desc_mist_d_2_l,
                mistN = R.string.weather_desc_mist_n_2_l)
    }

    private fun getExtremeColdLowDescription(type: String): String {
        return getWeatherForType(type,
                clearSkyD = R.string.weather_desc_clear_sky_d_1_l,
                clearSkyN = R.string.weather_desc_clear_sky_n_1_l,
                FewCloudsD = R.string.weather_desc_few_clouds_d_1_l,
                FewCloudsN = R.string.weather_desc_few_clouds_n_1_l,
                scatteredCloudsD = R.string.weather_desc_scattered_clouds_d_1_l,
                scatteredCloudsN = R.string.weather_desc_scattered_clouds_n_1_l,
                brokenCloudsD = R.string.weather_desc_broken_clouds_d_1_l,
                brokenCloudsN = R.string.weather_desc_broken_clouds_n_1_l,
                showerRainD = R.string.weather_desc_shower_rain_d_1_l,
                showerRainN = R.string.weather_desc_shower_rain_n_1_l,
                rainD = R.string.weather_desc_rain_d_1_l,
                rainN = R.string.weather_desc_rain_n_1_l,
                thunderstormD = R.string.weather_desc_thunderstorm_d_1_l,
                thunderstormN = R.string.weather_desc_thunderstorm_n_1_l,
                snowD = R.string.weather_desc_snow_d_1_l,
                snowN = R.string.weather_desc_snow_n_1_l,
                mistD = R.string.weather_desc_mist_d_1_l,
                mistN = R.string.weather_desc_mist_n_1_l)
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

    private fun getDescription(id: Int): String {
        return resources.getString(id)
    }

    private fun defaultMessage(): String {
        return resources.getString(R.string.weather_desc_default)
    }

}