package co.netguru.android.coolcal.preferences

import android.content.SharedPreferences
import co.netguru.android.coolcal.utils.into
import co.netguru.android.coolcal.weather.Pressure.UNIT_HPA
import co.netguru.android.coolcal.weather.Pressure.UNIT_MB
import co.netguru.android.coolcal.weather.Speed.UNIT_KMH
import co.netguru.android.coolcal.weather.Speed.UNIT_MPH
import co.netguru.android.coolcal.weather.Temperature.SIGN_DEGREE
import co.netguru.android.coolcal.weather.Temperature.UNIT_CELSIUS
import co.netguru.android.coolcal.weather.Temperature.UNIT_FAHRENHEIT
import java.util.*

class AppPreferences(val preferences: SharedPreferences, val locale: Locale) {

    companion object {
        private const val PREF_TEMP_UNIT = "pref_temp_unit"
        private const val PREF_TEMP_SIGN = "pref_temp_sign"
        private const val PREF_PRESSURE_UNIT = "pref_pressure_unit"
        private const val PREF_SPEED_UNIT = "pref_speed_unit"
    }

    private val isImperialLocale: Boolean =
            locale.isO3Country.equals("usa", ignoreCase = true)
                    || locale.isO3Country.equals("mmr", ignoreCase = true)

    private val defTempUnit = if (isImperialLocale) UNIT_FAHRENHEIT else UNIT_CELSIUS
    private val defPresUnit = if (isImperialLocale) UNIT_MB else UNIT_HPA
    private val defSpeedUnit = if (isImperialLocale) UNIT_MPH else UNIT_KMH


    var tempUnit: Int
        get() = preferences.getInt(PREF_TEMP_UNIT, defTempUnit)
        set(value) {
            into (preferences) {
                putInt(PREF_TEMP_UNIT, value)
            }
        }

    var tempSign: Int
        get() = preferences.getInt(PREF_TEMP_SIGN, SIGN_DEGREE)
        set(value) {
            into (preferences) {
                putInt(PREF_TEMP_SIGN, value)
            }
        }

    var pressureUnit: Int
        get() = preferences.getInt(PREF_PRESSURE_UNIT, defPresUnit)
        set(value) {
            into (preferences) {
                putInt(PREF_PRESSURE_UNIT, value)
            }
        }

    var speedUnit: Int
        get() = preferences.getInt(PREF_SPEED_UNIT, defSpeedUnit)
        set(value) {
            into (preferences) {
                putInt(PREF_SPEED_UNIT, value)
            }
        }
}