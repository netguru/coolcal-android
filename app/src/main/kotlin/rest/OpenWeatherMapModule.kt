package rest

import android.content.Context
import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.weather.OpenWeatherMap
import com.squareup.okhttp.Cache
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import javax.inject.Singleton

@Module
class OpenWeatherMapModule {

    companion object {
        const val CACHE_SIZE = 100 * 1024L
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache = Cache(context.cacheDir, CACHE_SIZE)

    @Provides
    @Singleton
    fun provideInterceptors(): List<Interceptor> =
            arrayListOf(OWMInterceptor(BuildConfig.OPENWEATHERMAP_API_KEY))

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, interceptors: List<Interceptor>): OkHttpClient {
        val client = OkHttpClient()
        client.cache = cache
        client.interceptors().addAll(interceptors)
        return client
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMap(client: OkHttpClient): OpenWeatherMap {
        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(OpenWeatherMap.API_ROOT)
                .build()
        return retrofit.create(OpenWeatherMap::class.java)
    }
}