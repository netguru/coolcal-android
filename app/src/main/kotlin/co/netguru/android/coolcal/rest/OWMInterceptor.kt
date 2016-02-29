package co.netguru.android.coolcal.rest

import co.netguru.android.coolcal.utils.logDebug
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.Response

class OwmInterceptor(val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain!!.request()

        val appidUrl = "${request.urlString()}&APPID=$apiKey"
        logDebug(appidUrl)

        val apiKeyRequest = request.newBuilder()
                .method(request.method(), request.body())
                .url(appidUrl)
        .build()
        //todo: check if owm response is cached, if not try adding CacheControl to response header

        return chain.proceed(apiKeyRequest);
    }

}