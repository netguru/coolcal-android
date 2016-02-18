package co.netguru.android.coolcal.app

import android.app.Application
import co.netguru.android.coolcal.formatting.FormattersModule
import co.netguru.android.coolcal.preferences.PreferencesModule
import co.netguru.android.coolcal.rest.RestModule
import com.squareup.leakcanary.LeakCanary

open class App : Application() {

    companion object {
        @JvmStatic lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .preferencesModule(PreferencesModule())
                .restModule(RestModule())
                .formattersModule(FormattersModule())
                .build()

        if (isLeakCanaryEnabled()) LeakCanary.install(this)
    }

    open protected fun isLeakCanaryEnabled() = true
}