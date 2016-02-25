package co.netguru.android.coolcal.app

import co.netguru.android.coolcal.calendar.CalendarTabView
import co.netguru.android.coolcal.calendar.EventHolder
import co.netguru.android.coolcal.calendar.EventTimelineView
import co.netguru.android.coolcal.calendar.TimelineHolder
import co.netguru.android.coolcal.formatting.FormattersModule
import co.netguru.android.coolcal.preferences.PreferencesModule
import co.netguru.android.coolcal.rest.RestModule
import co.netguru.android.coolcal.ui.EventsFragment
import co.netguru.android.coolcal.ui.WeatherFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        PreferencesModule::class,
        RestModule::class,
        FormattersModule::class
))
interface ApplicationComponent {

    fun inject(target: EventsFragment)
    fun inject(target: WeatherFragment)
    fun inject(target: EventHolder)
    fun inject(target: TimelineHolder)
    fun inject(target: CalendarTabView) // todo: make no @inject in view
    fun inject(target: EventTimelineView) // todo: jw
}