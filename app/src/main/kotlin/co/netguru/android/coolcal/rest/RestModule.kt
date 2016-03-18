package co.netguru.android.coolcal.rest

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
import javax.inject.Named
import javax.inject.Singleton

@Module
class RestModule {

    companion object {
        const val CACHE_SIZE = 100 * 1024L
        const val MAX_STALE = 10 * 60L // todo check
        const val MAX_AGE = 10 * 60L // todo check

        const val NAME_OWM_INTERCEPTOR = "owm_interceptor"
        const val NAME_CC_INTERCEPTOR = "cache_control_interceptor"
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache = Cache(context.cacheDir, CACHE_SIZE)

    @Provides
    @Singleton
    @Named(NAME_OWM_INTERCEPTOR)
    fun provideOwmInterceptor(): Interceptor = OwmInterceptor(BuildConfig.OPENWEATHERMAP_API_KEY)

    @Provides
    @Singleton
    @Named(NAME_CC_INTERCEPTOR)
    fun provideCacheControlInterceptor(): Interceptor = CacheControlInterceptor(MAX_STALE, MAX_AGE)

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache,
                            @Named(NAME_OWM_INTERCEPTOR)
                            owmInterceptor: Interceptor,
                            @Named(NAME_CC_INTERCEPTOR)
                            cacheControlInterceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient()
        client.cache = cache
        client.interceptors().add(owmInterceptor)
        client.networkInterceptors().add(cacheControlInterceptor)
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