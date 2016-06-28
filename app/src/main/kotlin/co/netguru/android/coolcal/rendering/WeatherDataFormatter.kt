package co.netguru.android.coolcal.rendering

/**
 * Base interface for formatting weather data such as temperature, pressure etc
 */
interface WeatherDataFormatter {

    /**
     * Outputs formatted temperature
     * @param tempKelvin temperature value in Kelvins
     */
    open fun formatTemperature(tempKelvin: Double?): String

    /**
     * Outputs formatted pressure
     * @param pressure pressure value in hPa (millibars)
     */
    open fun formatPressure(pressure: Double?): String

    /**
     * Outputs formatted wind speed and degrees of direction
     * @param speed speed of the wind in m/s
     * @param deg direction of the wind in degrees [0:360)
     */
    open fun formatWind(speed: Double?, deg: Double?): String
}