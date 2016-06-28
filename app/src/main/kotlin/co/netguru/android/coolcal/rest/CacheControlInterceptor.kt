package co.netguru.android.coolcal.rest

import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.Response

class CacheControlInterceptor(val maxStale: Long, val maxAge: Long) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {

        val request = chain!!.request();

        // Add Cache Control only for GET methods
        val modRequest = if (request.method().equals("GET"))
            request.newBuilder()
                    .header("Cache-Control", "public, max-stale=$maxStale")
                    .build();
        else
            request

        val response = chain.proceed(modRequest);

        // Re-write response CC header to force use of cache
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .build();
    }
}