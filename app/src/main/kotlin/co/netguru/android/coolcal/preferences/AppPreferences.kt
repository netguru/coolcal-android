package co.netguru.android.coolcal.preferences

import android.content.SharedPreferences
import co.netguru.android.coolcal.weather.Pressure
import co.netguru.android.coolcal.weather.Speed
import co.netguru.android.coolcal.weather.Speed.mph
import co.netguru.android.coolcal.weather.Temperature.SIGN_DEGREE
import co.netguru.android.coolcal.weather.Temperature.SIGN_FULL
import co.netguru.android.coolcal.weather.Temperature.SYMBOL_DEGREE
import co.netguru.android.coolcal.weather.Temperature.SYMBOL_DEGREE_CELSIUS
import co.netguru.android.coolcal.weather.Temperature.SYMBOL_DEGREE_FAHRENHEIT
import co.netguru.android.coolcal.weather.Temperature.SYMBOL_KELVIN
import co.netguru.android.coolcal.weather.Temperature.UNIT_CELSIUS
import co.netguru.android.coolcal.weather.Temperature.UNIT_FAHRENHEIT
import co.netguru.android.coolcal.weather.Temperature.UNIT_KELVIN
import co.netguru.android.coolcal.weather.Temperature.celsius
import co.netguru.android.coolcal.weather.Temperature.fahrenheit
import co.netguru.android.coolcal.weather.Wind
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*
import javax.inject.Inject


object AppPreferences {

    const val SYMBOL_UNDEFINED = "N/A"

    private const val KEY_PREFERENCES = "coolcal_preferences"
    private const val PREF_TEMP_UNIT = "pref_temp_unit"
    private const val PREF_TEMP_SIGN = "pref_temp_unit"
    private const val PREF_PRESSURE_UNIT = "pref_pressure_unit"
    private const val PREF_SPEED_UNIT = "pref_speed_unit"

    @Inject lateinit var preferences: SharedPreferences

    private val defaultTemperatureUnit: Int by lazy { assumeDefaultTempUnit() }
    private val defaultPressureUnit: Int by lazy { assumeDefaultPressureUnit() }
    private val defaultSpeedUnit: Int by lazy { assumeDefaultSpeedUnit() }

    var tempUnit: Int
        get() = preferences.getInt(PREF_TEMP_UNIT, defaultTemperatureUnit)
        set(value) {
            preferences.edit().putInt(PREF_TEMP_UNIT, value).apply()
        }

    var tempSign: Int
        get() = preferences.getInt(PREF_TEMP_SIGN, SIGN_DEGREE)
        set(value) {
            preferences.edit().putInt(PREF_TEMP_SIGN, value).apply()
        }

    var pressureUnit: Int
        get() = preferences.getInt(PREF_PRESSURE_UNIT, defaultPressureUnit)
        set(value) {
            preferences.edit().putInt(PREF_PRESSURE_UNIT, value).apply()
        }

    var speedUnit: Int
        get() = preferences.getInt(PREF_SPEED_UNIT, defaultSpeedUnit)
        set(value) {
            preferences.edit().putInt(PREF_SPEED_UNIT, value).apply()
        }

    private val periodFormatter: PeriodFormatter by lazy {
        PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix("h")
                .appendSeparator(" ")
                .appendMinutes()
                .appendSuffix("m")
                .toFormatter()
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

    fun formatTemperature(tempKelvin: Double?): String {
        if (tempKelvin == null) return SYMBOL_UNDEFINED
        val unit = tempUnit
        val mode = tempSign
        val temp = when (unit) {
            UNIT_CELSIUS -> "${Math.round(tempKelvin.celsius())}"
            UNIT_FAHRENHEIT -> "${Math.round(tempKelvin.fahrenheit())}"
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

    fun formatPressure(pressure: Double?): String {
        if (pressure == null) return SYMBOL_UNDEFINED
        val pres = Math.round(pressure)
        val unit = when (pressureUnit) {
            Pressure.UNIT_MB -> Pressure.SYMBOL_MB
            else -> Pressure.SYMBOl_HPA
        }
        return "$pres$unit"
    }

    fun formatWind(wind: Wind?): String {
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
                val mph = Math.round(wind.speed!!.mph())
                "$mph ${Speed.SYMBOL_MPH}"
            }
            else -> {
                val kmh = Math.round(wind.speed!!)
                "$kmh ${Speed.SYMBOl_KMH}"
            }
        }
        return "$speedString $cardinal"
    }

    fun formatDateTime(dt: Long, pattern: String) =
            DateTime(dt).toLocalDateTime().toString(pattern, Locale.getDefault())

    fun formatTimeOfDay(dt: Long) = formatDateTime(dt, "H:mm")

    fun formatDayOfMonth(dt: Long) = formatDateTime(dt, "dd")

    fun formatDayOfWeek(dt: Long) = formatDateTime(dt, "EEEE")

    fun formatDayOfWeekShort(dt: Long) = formatDateTime(dt, "EEE")

    fun formatPeriod(dtStart: Long, dtEnd: Long) =
            Period(dtStart, dtEnd).toString(periodFormatter)
}