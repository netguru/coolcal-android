@file:Suppress("unused")
package co.netguru.android.coolcal.weather

object Speed {

    /*
        Velocity units
    */
    const val UNIT_MS = 0
    const val UNIT_KMH = 1
    const val UNIT_MPH = 2

    /*
        Pressure symbols
     */
    const val SYMBOL_MS = "m/s"
    const val SYMBOl_KMH = "km/h"
    const val SYMBOL_MPH = "mph"

    fun Double.kmh() = this * 3.6 // todo: check the actual unit of the owm response speed

    fun Double.mph() = this * 2.23694

}