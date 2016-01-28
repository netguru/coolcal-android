package co.netguru.android.coolcal.utils

import android.content.Context
import android.content.SharedPreferences
import co.netguru.android.coolcal.utils.Temperature.SIGN_DEGREE
import co.netguru.android.coolcal.utils.Temperature.SIGN_FULL
import co.netguru.android.coolcal.utils.Temperature.SYMBOL_DEGREE
import co.netguru.android.coolcal.utils.Temperature.SYMBOL_DEGREE_CELSIUS
import co.netguru.android.coolcal.utils.Temperature.SYMBOL_DEGREE_FAHRENHEIT
import co.netguru.android.coolcal.utils.Temperature.SYMBOL_KELVIN
import co.netguru.android.coolcal.utils.Temperature.UNIT_CELSIUS
import co.netguru.android.coolcal.utils.Temperature.UNIT_FAHRENHEIT
import co.netguru.android.coolcal.utils.Temperature.UNIT_KELVIN
import co.netguru.android.coolcal.utils.Temperature.kelvinToCelsius
import co.netguru.android.coolcal.utils.Temperature.kelvinToFahrenheit
import co.netguru.android.coolcal.weather.Wind
import org.joda.time.DateTime
import java.util.*

object AppPreferences {

    const val SYMBOL_UNDEFINED = "N/A"

    private const val KEY_PREFERENCES = "coolcal_preferences"
    private const val PREF_TEMP_UNIT = "pref_temp_unit"
    private const val PREF_TEMP_SIGN = "pref_temp_unit"
    private const val PREF_PRESSURE_UNIT = "pref_pressure_unit"
    private const val PREF_SPEED_UNIT = "pref_speed_unit"

    private var preferences: SharedPreferences? = null

    private val defaultTemperatureUnit: Int by lazy { assumeDefaultTempUnit() }
    private val defaultPressureUnit: Int by lazy { assumeDefaultPressureUnit() }
    private val defaultSpeedUnit: Int by lazy { assumeDefaultSpeedUnit() }

    public var tempUnit: Int
        get() = preferences!!.getInt(PREF_TEMP_UNIT, defaultTemperatureUnit)
        set(value) {
            preferences!!.edit().putInt(PREF_TEMP_UNIT, value).apply()
        }

    public var tempSign: Int
        get() = preferences!!.getInt(PREF_TEMP_SIGN, SIGN_DEGREE)
        set(value) {
            preferences!!.edit().putInt(PREF_TEMP_SIGN, value).apply()
        }

    public var pressureUnit: Int
        get() = preferences!!.getInt(PREF_PRESSURE_UNIT, defaultPressureUnit)
        set(value) {
            preferences!!.edit().putInt(PREF_PRESSURE_UNIT, value).apply()
        }

    public var speedUnit: Int
        get() = preferences!!.getInt(PREF_SPEED_UNIT, defaultSpeedUnit)
        set(value) {
            preferences!!.edit().putInt(PREF_SPEED_UNIT, value).apply()
        }

    internal fun init(context: Context) {
        preferences = context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)
    }

    /**
     * Check if the default locale is USA or Myanmar (imperial units)
     */
    fun isImperialLocale(locale: Locale) = locale.isO3Country.equals("usa", ignoreCase = true)
            || locale.isO3Country.equals("mmr", ignoreCase = true)

    private fun assumeDefaultTempUnit(): Int =
            if (isImperialLocale(locale = Locale.getDefault())) {
                UNIT_FAHRENHEIT
            } else {
                UNIT_CELSIUS
            }

    private fun assumeDefaultPressureUnit(): Int =
            if (isImperialLocale(locale = Locale.getDefault())) {
                Pressure.UNIT_MB
            } else {
                Pressure.UNIT_HPA
            }

    private fun assumeDefaultSpeedUnit(): Int =
            if (isImperialLocale(locale = Locale.getDefault())) {
                Speed.UNIT_MPH
            } else {
                Speed.UNIT_KMH
            }

    public fun formatTemperature(tempKelvin: Double?): String {
        if (tempKelvin == null) return SYMBOL_UNDEFINED
        val unit = tempUnit
        val mode = tempSign
        val temp = when (unit) {
            UNIT_CELSIUS -> "${Math.round(kelvinToCelsius(tempKelvin))}"
            UNIT_FAHRENHEIT -> "${Math.round(kelvinToFahrenheit(tempKelvin))}"
            else -> "$tempKelvin"
        }
        val sign = when (mode) {
            SIGN_DEGREE -> if (unit == UNIT_KELVIN) "" else SYMBOL_DEGREE
            SIGN_FULL -> when (unit) {
                UNIT_CELSIUS -> SYMBOL_DEGREE_CELSIUS
                UNIT_FAHRENHEIT -> SYMBOL_DEGREE_FAHRENHEIT
                else -> SYMBOL_KELVIN
            }
            else /*SIGN_NONE*/ -> ""
        }
        return "$temp$sign"
    }

    public fun formatPressure(pressure: Double?): String {
        if (pressure == null) return SYMBOL_UNDEFINED
        val pres = Math.round(pressure)
        val unit = when (pressureUnit) {
            Pressure.UNIT_MB -> Pressure.SYMBOL_MB
            else -> Pressure.SYMBOl_HPA
        }
        return "$pres$unit"
    }

    public fun formatWind(wind: Wind?): String {
        if (wind == null || wind.speed == null) return SYMBOL_UNDEFINED
        val cardinal = when (wind.deg) {
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
        val speedString = when (speedUnit) {
            Speed.UNIT_MPH -> {
                val mph = Math.round(Speed.kmhToMph(wind.speed!!))
                "$mph ${Speed.SYMBOL_MPH}"
            }
            else -> {
                val kmh = Math.round(wind.speed!!)
                "$kmh ${Speed.SYMBOl_KMH}"
            }
        }
        return "$speedString $cardinal"
    }

    public fun formatTime(dt: Long): String {
        val dateTime = DateTime(dt).toLocalDateTime()
        return dateTime.toString("H:mm", Locale.getDefault())
    }
}