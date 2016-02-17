package co.netguru.android.coolcal.app

import co.netguru.android.coolcal.preferences.PreferencesModule
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class, PreferencesModule::class))
interface ApplicationComponent {


}