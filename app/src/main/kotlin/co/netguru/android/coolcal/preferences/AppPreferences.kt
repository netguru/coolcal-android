package co.netguru.android.coolcal.preferences

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
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

    private val isImperialLocale: Boolean =
            locale.isO3Country.equals("usa", ignoreCase = true)
                    || locale.isO3Country.equals("mmr", ignoreCase = true)

    private val defTempUnit = if (isImperialLocale) UNIT_FAHRENHEIT else UNIT_CELSIUS
    private val defPresUnit = if (isImperialLocale) Pressure.UNIT_MB else Pressure.UNIT_HPA
    private val defSpeedUnit = if (isImperialLocale) Speed.UNIT_MPH else Speed.UNIT_KMH


    var tempUnit: Int
        get() = preferences.getInt(PREF_TEMP_UNIT, defTempUnit)
        set(value) {
            preferences.edit { putInt(PREF_TEMP_UNIT, value) }
        }

    var tempSign: Int
        get() = preferences.getInt(PREF_TEMP_SIGN, SIGN_DEGREE)
        set(value) {
            preferences.edit { putInt(PREF_TEMP_SIGN, value) }
        }

    var pressureUnit: Int
        get() = preferences.getInt(PREF_PRESSURE_UNIT, defPresUnit)
        set(value) {
            preferences.edit { putInt(PREF_PRESSURE_UNIT, value) }
        }

    var speedUnit: Int
        get() = preferences.getInt(PREF_SPEED_UNIT, defSpeedUnit)
        set(value) {
            preferences.edit { putInt(PREF_SPEED_UNIT, value) }
        }
}

inline fun SharedPreferences.edit(crossinline block: Editor.() -> Editor) {
    this.edit().block().apply()
}