package co.netguru.android.coolcal.rendering

import android.content.res.Resources
import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21),
        manifest = "src/main/AndroidManifest.xml",
        packageName = "co.netguru.android.coolcal")
class WeatherDescriptionHelperTest {

    val lowPressure = (WeatherDescriptionHelper.HIGH_PRESSURE - 1).toDouble()
    val highPressure = (WeatherDescriptionHelper.HIGH_PRESSURE + 1).toDouble()
    lateinit var res: Resources
    lateinit var weatherDescriptionHelper: WeatherDescriptionHelper

    @Before
    fun prepare() {
        res = RuntimeEnvironment.application.resources
        weatherDescriptionHelper = WeatherDescriptionHelper(res)
    }

    @Test
    fun testGetDescriptionLowPressure() {
        //Level 1 of temperature
        val type = WeatherCodes.BROKEN_CLOUDS_NIGHT
        val temp = (-5.0).toKelvin()
        val temp1 = (-4.28).toKelvin()
        val expected = res.getString(R.string.weather_desc_broken_clouds_n_1_l)

        //Level 2 of temperature
        val type2 = WeatherCodes.CLEAR_SKY_DAY
        val temp2 = (-4.0).toKelvin()
        val temp22 = (0.99).toKelvin()
        val expected2 = res.getString(R.string.weather_desc_clear_sky_d_2_l)

        //Level 3 of temperature
        val type3 = WeatherCodes.MIST_DAY
        val temp3 = 1.0.toKelvin()
        val temp32 = 8.5.toKelvin()
        val expected3 = res.getString(R.string.weather_desc_mist_d_3_l)

        //Level 4 of temperature
        val type4 = WeatherCodes.RAIN_NIGHT
        val temp4 = 9.0.toKelvin()
        val temp42 = 16.99.toKelvin()
        val expected4 = res.getString(R.string.weather_desc_rain_n_4_l)

        //Level 5 of temperature
        val type5 = WeatherCodes.FEW_CLOUDS_DAY
        val temp5 = 17.0.toKelvin()
        val temp52 = 24.8.toKelvin()
        val expected5 = res.getString(R.string.weather_desc_few_clouds_d_5_l)

        //Level 6 of temperature
        val type6 = WeatherCodes.SCATTERED_CLOUDS_NIGHT
        val temp6 = 25.0.toKelvin()
        val temp62 = 32.34.toKelvin()
        val expected6 = res.getString(R.string.weather_desc_scattered_clouds_n_6_l)

        //Level 7 of temperature
        val type7 = WeatherCodes.SCATTERED_CLOUDS_DAY
        val temp7 = 33.0.toKelvin()
        val expected7 = res.getString(R.string.weather_desc_scattered_clouds_d_7_l)

        assertEquals(expected, weatherDescriptionHelper.getDescription(type, temp, lowPressure))
        assertEquals(expected, weatherDescriptionHelper.getDescription(type, temp1, lowPressure))
        assertEquals(expected2, weatherDescriptionHelper.getDescription(type2, temp2, lowPressure))
        assertEquals(expected2, weatherDescriptionHelper.getDescription(type2, temp22, lowPressure))
        assertEquals(expected3, weatherDescriptionHelper.getDescription(type3, temp3, lowPressure))
        assertEquals(expected3, weatherDescriptionHelper.getDescription(type3, temp32, lowPressure))
        assertEquals(expected4, weatherDescriptionHelper.getDescription(type4, temp4, lowPressure))
        assertEquals(expected4, weatherDescriptionHelper.getDescription(type4, temp42, lowPressure))
        assertEquals(expected5, weatherDescriptionHelper.getDescription(type5, temp5, lowPressure))
        assertEquals(expected5, weatherDescriptionHelper.getDescription(type5, temp52, lowPressure))
        assertEquals(expected6, weatherDescriptionHelper.getDescription(type6, temp6, lowPressure))
        assertEquals(expected6, weatherDescriptionHelper.getDescription(type6, temp62, lowPressure))
        assertEquals(expected7, weatherDescriptionHelper.getDescription(type7, temp7, lowPressure))

    }

    @Test
    fun testGetDescriptionHighPressure() {
        //Level 1 of temperature
        val type = WeatherCodes.MIST_DAY
        val temp = (-10.0).toKelvin()
        val temp1 = (-4.8).toKelvin()
        val expected = res.getString(R.string.weather_desc_mist_d_1_h)

        //Level 2 of temperature
        val type2 = WeatherCodes.RAIN_NIGHT
        val temp2 = (0.0).toKelvin()
        val temp22 = (0.8).toKelvin()
        val expected2 = res.getString(R.string.weather_desc_rain_n_2_h)

        //Level 3 of temperature
        val type3 = WeatherCodes.MIST_NIGHT
        val temp3 = 8.0.toKelvin()
        val temp32 = 8.1.toKelvin()
        val expected3 = res.getString(R.string.weather_desc_mist_n_3_h)

        //Level 4 of temperature
        val type4 = WeatherCodes.THUNDERSTORM_NIGHT
        val temp4 = 16.0.toKelvin()
        val temp42 = 16.7777.toKelvin()
        val expected4 = res.getString(R.string.weather_desc_thunderstorm_n_4_h)

        //Level 5 of temperature
        val type5 = WeatherCodes.SHOWER_RAIN_DAY
        val temp5 = 24.0.toKelvin()
        val temp52 = 24.577.toKelvin()
        val expected5 = res.getString(R.string.weather_desc_shower_rain_d_5_h)

        //Level 6 of temperature
        val type6 = WeatherCodes.SCATTERED_CLOUDS_DAY
        val temp6 = 32.0.toKelvin()
        val temp62 = 32.111.toKelvin()
        val expected6 = res.getString(R.string.weather_desc_scattered_clouds_d_6_h)

        //Level 7 of temperature
        val type7 = WeatherCodes.FEW_CLOUDS_DAY
        val temp7 = 39.0.toKelvin()
        val expected7 = res.getString(R.string.weather_desc_few_clouds_d_7_h)

        assertEquals(expected, weatherDescriptionHelper.getDescription(type, temp, highPressure))
        assertEquals(expected, weatherDescriptionHelper.getDescription(type, temp1, highPressure))
        assertEquals(expected2, weatherDescriptionHelper.getDescription(type2, temp2, highPressure))
        assertEquals(expected2, weatherDescriptionHelper.getDescription(type2, temp22, highPressure))
        assertEquals(expected3, weatherDescriptionHelper.getDescription(type3, temp3, highPressure))
        assertEquals(expected3, weatherDescriptionHelper.getDescription(type3, temp32, highPressure))
        assertEquals(expected4, weatherDescriptionHelper.getDescription(type4, temp4, highPressure))
        assertEquals(expected4, weatherDescriptionHelper.getDescription(type4, temp42, highPressure))
        assertEquals(expected5, weatherDescriptionHelper.getDescription(type5, temp5, highPressure))
        assertEquals(expected5, weatherDescriptionHelper.getDescription(type5, temp52, highPressure))
        assertEquals(expected6, weatherDescriptionHelper.getDescription(type6, temp6, highPressure))
        assertEquals(expected6, weatherDescriptionHelper.getDescription(type6, temp62, highPressure))
        assertEquals(expected7, weatherDescriptionHelper.getDescription(type7, temp7, highPressure))

    }

    /**
     * Converts from celsius to kelvin. Just for tests purpose.
     */
    private fun Double.toKelvin() = this + 273.15
}
