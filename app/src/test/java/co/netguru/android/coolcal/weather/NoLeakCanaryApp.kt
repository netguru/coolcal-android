package co.netguru.android.coolcal.weather

import co.netguru.android.coolcal.app.App

open class NoLeakCanaryApp : App() {

    override fun isLeakCanaryEnabled() = false
}

