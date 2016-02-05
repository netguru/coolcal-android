package co.netguru.android.coolcal.utils

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

    fun mphToKmh(mph: Double) = mph / 0.621371

    fun kmhToMph(kmh: Double) = kmh * 0.621371

}