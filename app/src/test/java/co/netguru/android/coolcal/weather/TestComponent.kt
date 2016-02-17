package co.netguru.android.coolcal.weather

import dagger.Component
import rest.OpenWeatherMapModule

@Component(modules = arrayOf(OpenWeatherMapModule::class))
interface TestComponent {

    fun inject(target: TestNoLeakCanaryApp): Unit
}