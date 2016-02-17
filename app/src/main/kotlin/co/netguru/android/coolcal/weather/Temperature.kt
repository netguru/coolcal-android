package co.netguru.android.coolcal.weather

object Temperature {

    /*
        Temperature units
     */
    const val UNIT_KELVIN = 0
    const val UNIT_CELSIUS = 1
    const val UNIT_FAHRENHEIT = 2

    /*
        Unit sign modes
     */
    const val SIGN_NONE = 0
    const val SIGN_DEGREE = 1;
    const val SIGN_FULL = 2;

    /*
        Temperature symmbols
     */
    const val SYMBOL_DEGREE = "\u00B0"
    const val SYMBOL_DEGREE_CELSIUS = "\u2103"
    const val SYMBOL_DEGREE_FAHRENHEIT = "\u2109"
    const val SYMBOL_KELVIN = "\u212A"

    // kelvin
    fun Double.celsius() = this - 273.15

    fun Double.fahrenheit() = this * 1.8 - 459.67
}