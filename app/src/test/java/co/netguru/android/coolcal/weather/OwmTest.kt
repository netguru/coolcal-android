package co.netguru.android.coolcal.weather

import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.NetworkServiceHelper
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21),
        manifest = "src/main/AndroidManifest.xml",
        packageName = "co.netguru.android.coolcal")
class TestNoLeakCanaryApp : NoLeakCanaryApp() {

    lateinit var openWeatherMap: OpenWeatherMap

    private val city = "Kraków"

    @Before
    fun prepare() {
        val retrofit = NetworkServiceHelper().provideRetrofit(OpenWeatherMap.API_ROOT)
        openWeatherMap = retrofit.create(OpenWeatherMap::class.java)
    }

    @Throws(Exception::class)
    fun <T> testCall(obs: Observable<T>) {
        val testSubscriber = TestSubscriber<T>()

        obs.subscribe(testSubscriber)

        testSubscriber.apply {
            assertValueCount(1)
            assertCompleted()
        }
    }

    @Test
    @Throws(Exception::class)
    fun testForecastCall() {
        testCall(openWeatherMap.getForecast(city))
    }

    @Test
    @Throws(Exception::class)
    fun testWeatherCall() {
        testCall(openWeatherMap.getWeather(city))
    }

    @Test
    @Throws(Exception::class)
    fun testWeatherResponse() {
        openWeatherMap.getWeather(city)
                .toBlocking()
                .forEach { response ->
                    response.apply {
                        assertNotNull(coord)
                        assertNotNull(cityName)
                        assertNotNull(weather[0])
                        assertNotNull(main)
                        assertNotNull(wind)
                        assertNotNull(sys)
                    }

                    System.out.println(response.toString())
                }
    }

    @Test
    @Throws(Exception::class)
    fun testForecastResponse() {
        openWeatherMap.getForecast(city, count = 1)
                .toBlocking()
                .forEach { response ->
                    response.forecastList.forEach {
                        forecast ->
                        forecast.apply {
                            assertNotNull(main)
                            assertNotNull(weatherList[0])
                            assertNotNull(dateString)
                        }

                        System.out.println(response.toString())
                    }
                }
    }
}
