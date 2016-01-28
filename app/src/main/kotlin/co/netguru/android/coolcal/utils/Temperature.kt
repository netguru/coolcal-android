package co.netguru.android.coolcal.utils

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
    public fun kelvinToCelsius(k: Double) = k - 273.15

    public fun kelvinToFahrenheit(k: Double) = k * 1.8 - 459.67

    // celsius
    public fun celsiusToKelvin(c: Double) = c - 273.15

    public fun celsiusToFahrenheit(c: Double) = c * 1.8 + 32

    // fahrenheit
    public fun fahrenheitToKelvin(f: Double) = (f - 32) * 5 / 9

    public fun fahrenheitToCelsius(f: Double) = (f + 459.67) * 5 / 9
}