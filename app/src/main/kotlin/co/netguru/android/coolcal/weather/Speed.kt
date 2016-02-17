package co.netguru.android.coolcal.weather

object Speed {

    /*
        Velocity units
    */
    const val UNIT_KMH = 0
    const val UNIT_MPH = 1

    /*
        Pressure symbols
     */
    const val SYMBOl_KMH = "km/h"
    const val SYMBOL_MPH = "mph"

    fun Double.kmh() = this / 0.621371

    fun Double.mph() = this * 0.621371

}