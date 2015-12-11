package co.netguru.android.owm.api

import co.netguru.android.owm.BuildConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber
import kotlin.test.assertNotNull

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class OwmTest {

    val testApiKey = "2de143494c0b295cca9337e1e96b00e0"
    val owm = OpenWeatherMap.api

    @Before
    fun prepareApi() {
        OpenWeatherMap.client.interceptors().add(OWMInterceptor(testApiKey))
    }

    @Throws(Exception::class)
    fun<T> testCall(obs: Observable<T>) {
        val testSubscriber = TestSubscriber<T>()

        obs.subscribe(testSubscriber)

        testSubscriber.assertValueCount(1)
        testSubscriber.assertCompleted()
    }

    @Test
    @Throws(Exception::class)
    fun testCalls() {
        testCall(owm.getWeather("Kraków"))
        testCall(owm.getForecast("Kraków"))
    }

    @Test
    @Throws(Exception::class)
    fun testWeatherResponse() {
        owm.getWeather("Kraków")
                .toBlocking()
                .forEach { response ->
                    assertNotNull(response.coord)
                    assertNotNull(response.cityName)
                    assertNotNull(response.weather[0])
                    assertNotNull(response.main)
                    assertNotNull(response.wind)
                    assertNotNull(response.sys)
                    System.out.println(response.toString())
                }
    }

    @Test
    @Throws(Exception::class)
    fun testForecastResponse() {
        owm.getForecast("Kraków", count = 1)
                .toBlocking()
                .forEach { response ->
                    response.forecastList.forEach {
                        forecast ->
                            assertNotNull(forecast.main)
                        assertNotNull(forecast.weatherList[0])
                        assertNotNull(forecast.dateString)
                    }
                    System.out.println(response.toString())
                }
    }
}
