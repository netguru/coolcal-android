package co.netguru.android.coolcal.rendering

import co.netguru.android.coolcal.preferences.AppPreferences
import co.netguru.android.coolcal.weather.Pressure
import co.netguru.android.coolcal.weather.Speed
import co.netguru.android.coolcal.weather.Speed.mph
import co.netguru.android.coolcal.weather.Temperature
import co.netguru.android.coolcal.weather.Temperature.celsius
import co.netguru.android.coolcal.weather.Temperature.fahrenheit

class WeatherDataFormatterImpl(val appPreferences: AppPreferences) : WeatherDataFormatter {

    companion object {
        const val SYMBOL_UNDEFINED = "N/A"
    }

    override fun formatTemperature(tempKelvin: Double?): String {
        if (tempKelvin == null) return SYMBOL_UNDEFINED
        val unit = appPreferences.tempUnit
        val mode = appPreferences.tempSign
        val temp = when (unit) {
            Temperature.UNIT_CELSIUS -> "${Math.round(tempKelvin.celsius())}"
            Temperature.UNIT_FAHRENHEIT -> "${Math.round(tempKelvin.fahrenheit())}"
            else -> "$tempKelvin"
        }
        val sign = when (mode) {
            Temperature.SIGN_DEGREE -> if (unit == Temperature.UNIT_KELVIN) "" else Temperature.SYMBOL_DEGREE
            Temperature.SIGN_FULL -> when (unit) {
                Temperature.UNIT_CELSIUS -> Temperature.SYMBOL_DEGREE_CELSIUS
                Temperature.UNIT_FAHRENHEIT -> Temperature.SYMBOL_DEGREE_FAHRENHEIT
                else -> Temperature.SYMBOL_KELVIN
            }
            else /*SIGN_NONE*/ -> ""
        }
        return "$temp$sign"
    }

    override fun formatPressure(pressure: Double?): String {
        if (pressure == null) return SYMBOL_UNDEFINED
        val pres = Math.round(pressure)
        val unit = when (appPreferences.pressureUnit) {
            Pressure.UNIT_MB -> Pressure.SYMBOL_MB
            else -> Pressure.SYMBOl_HPA
        }
        return "$pres$unit"
    }

    override fun formatWind(speed: Double?, deg: Double?): String {
        if (speed == null) return SYMBOL_UNDEFINED
        val cardinal = when (deg) {
            null -> ""
            in 22.5..67.5 -> "NE"
            in 67.5..112.5 -> "E"
            in 112.5..157.5 -> "SE"
            in 157.5..202.5 -> "S"
            in 202.5..247.5 -> "SW"
            in 247.5..292.5 -> "W"
            in 292.5..337.5 -> "NW"
            else -> "N"
        }
        val speedString = when (appPreferences.speedUnit) {
            Speed.UNIT_MPH -> {
                val mph = Math.round(speed.mph())
                "$mph ${Speed.SYMBOL_MPH}"
            }
            Speed.UNIT_KMH -> {
                val kmh = Math.round(speed)
                "$kmh ${Speed.SYMBOl_KMH}"
            }
            else -> "${speed} ${Speed.SYMBOL_MS}"
        }
        return "$speedString $cardinal"
    }
}