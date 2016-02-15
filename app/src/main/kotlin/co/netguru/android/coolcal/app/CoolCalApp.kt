package co.netguru.android.coolcal.app

import android.app.Application
import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.utils.AppPreferences
import co.netguru.android.coolcal.weather.OWMInterceptor
import co.netguru.android.coolcal.weather.OpenWeatherMap
import com.squareup.leakcanary.LeakCanary

open class CoolCalApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppPreferences.init(this)

        val owmApiKey = BuildConfig.OPENWEATHERMAP_API_KEY
        OpenWeatherMap.client.interceptors().add(OWMInterceptor(owmApiKey))

        if (isLeakCanaryEnabled()) LeakCanary.install(this)
    }

    open protected fun isLeakCanaryEnabled() = true
}