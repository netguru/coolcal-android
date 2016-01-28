package co.netguru.android.coolcal.weather

import com.squareup.okhttp.OkHttpClient
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

object OpenWeatherMap {

    val client = OkHttpClient()

    val api: OpenWeatherMapApi by lazy {

        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(OpenWeatherMapApi.WEATHER_API_ROOT)
                .build()

        retrofit.create(OpenWeatherMapApi::class.java)
    }
}
