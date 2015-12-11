package co.netguru.android.owm.api

import android.util.Log
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.Response

class OWMInterceptor(val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain!!.request()

        val appidUrl = "${request.urlString()}&APPID=$apiKey"
        Log.d("Requesting", appidUrl)

        val apiKeyRequest = request.newBuilder()
                .method(request.method(), request.body())
                .url(appidUrl)
        .build()

        return chain.proceed(apiKeyRequest);
    }

}