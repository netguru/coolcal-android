package co.netguru.android.coolcal.weather

import co.netguru.android.coolcal.app.CoolCalApp

open class NoLeakCanaryApp : CoolCalApp() {

    override fun isLeakCanaryEnabled() = false
}

