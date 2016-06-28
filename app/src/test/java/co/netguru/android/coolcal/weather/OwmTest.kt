package co.netguru.android.coolcal.weather

import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.rest.CacheControlInterceptor
import co.netguru.android.coolcal.rest.OwmInterceptor
import com.squareup.okhttp.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21),
        manifest = "src/main/AndroidManifest.xml",
        packageName = "co.netguru.android.coolcal")
class TestNoLeakCanaryApp : NoLeakCanaryApp() {

    lateinit var openWeatherMap: OpenWeatherMap

    @Before
    fun prepare() {
        val interceptor = OwmInterceptor(BuildConfig.OPENWEATHERMAP_API_KEY)
        val client = OkHttpClient()
        client.interceptors().add(interceptor)
        client.networkInterceptors().add(CacheControlInterceptor(10 * 60L, 10 * 60L))
        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(OpenWeatherMap.API_ROOT)
                .build()
        openWeatherMap = retrofit.create(OpenWeatherMap::class.java)
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
        testCall(openWeatherMap.getWeather("Kraków"))
        testCall(openWeatherMap.getForecast("Kraków"))
    }

    @Test
    @Throws(Exception::class)
    fun testWeatherResponse() {
        openWeatherMap.getWeather("Kraków")
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
        openWeatherMap.getForecast("Kraków", count = 1)
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
