package co.netguru.android.coolcal.preferences

import android.content.SharedPreferences
import co.netguru.android.coolcal.weather.Pressure
import co.netguru.android.coolcal.weather.Speed
import co.netguru.android.coolcal.weather.Temperature.SIGN_DEGREE
import co.netguru.android.coolcal.weather.Temperature.UNIT_CELSIUS
import co.netguru.android.coolcal.weather.Temperature.UNIT_FAHRENHEIT
import java.util.*


class AppPreferences(val preferences: SharedPreferences, val locale: Locale) {

    companion object {
        private const val PREF_TEMP_UNIT = "pref_temp_unit"
        private const val PREF_TEMP_SIGN = "pref_temp_unit"
        private const val PREF_PRESSURE_UNIT = "pref_pressure_unit"
        private const val PREF_SPEED_UNIT = "pref_speed_unit"
    }

    private val defaultTemperatureUnit: Int by lazy { assumeDefaultTempUnit() }
    private val defaultPressureUnit: Int by lazy { assumeDefaultPressureUnit() }
    private val defaultSpeedUnit: Int by lazy { assumeDefaultSpeedUnit() }
    private val isImperialLocale: Boolean by lazy {
        locale.isO3Country.equals("usa", ignoreCase = true)
                || locale.isO3Country.equals("mmr", ignoreCase = true)
    }

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

    private fun assumeDefaultTempUnit(): Int =
            if (isImperialLocale) {
                UNIT_FAHRENHEIT
            } else {
                UNIT_CELSIUS
            }

    private fun assumeDefaultPressureUnit(): Int =
            if (isImperialLocale) {
                Pressure.UNIT_MB
            } else {
                Pressure.UNIT_HPA
            }

    private fun assumeDefaultSpeedUnit(): Int =
            if (isImperialLocale) {
                Speed.UNIT_MPH
            } else {
                Speed.UNIT_KMH
            }
}