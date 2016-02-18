package co.netguru.android.coolcal.formatting

import co.netguru.android.coolcal.weather.Wind

interface ValueFormatter {

    open fun formatTemperature(tempKelvin: Double?): String

    open fun formatPressure(pressure: Double?): String

    open fun formatWind(wind: Wind?): String
}