package co.netguru.android.coolcal.app

import android.app.Application
import co.netguru.android.coolcal.R
import co.netguru.android.owm.api.OWMInterceptor
import co.netguru.android.owm.api.OpenWeatherMap
import com.squareup.leakcanary.LeakCanary

class CoolCalApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val owmApiKey = getString(R.string.owmApiKey)
        OpenWeatherMap.client.interceptors().add(OWMInterceptor(owmApiKey))

        LeakCanary.install(this)
    }
}