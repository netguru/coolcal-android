package co.netguru.android.coolcal

import co.netguru.android.coolcal.rest.CacheControlInterceptor
import co.netguru.android.coolcal.rest.OwmInterceptor
import com.squareup.okhttp.OkHttpClient
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory


class NetworkServiceHelper {

     private fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient().apply {
            interceptors().add(OwmInterceptor(BuildConfig.OPENWEATHERMAP_API_KEY))
            networkInterceptors().add(CacheControlInterceptor(10 * 60L, 10 * 60L))
        }
    }

    internal fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .client(provideOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }
}
