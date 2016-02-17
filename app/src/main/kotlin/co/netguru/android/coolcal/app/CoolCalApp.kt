package co.netguru.android.coolcal.app

import android.app.Application
import com.squareup.leakcanary.LeakCanary

open class CoolCalApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (isLeakCanaryEnabled()) LeakCanary.install(this)
    }

    open protected fun isLeakCanaryEnabled() = true
}