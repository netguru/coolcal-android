package co.netguru.android.coolcal.formatting

import co.netguru.android.coolcal.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*
import javax.inject.Singleton

@Module
class FormattersModule {

    @Provides
    fun providePeriodFormatter() =
            PeriodFormatterBuilder()
                    .appendHours()
                    .appendSuffix("h")
                    .appendSeparator(" ")
                    .appendMinutes()
                    .appendSuffix("m")
                    .toFormatter()

    @Provides
    fun provideLocale() = Locale.getDefault()

    @Provides
    @Singleton
    fun provideTimeFormatter(locale: Locale, periodFormatter: PeriodFormatter): TimeFormatter =
            TimeFormatterImpl(locale, periodFormatter)

    @Provides
    @Singleton
    fun provideValueFormatter(appPreferences: AppPreferences): ValueFormatter =
            ValueFormatterImpl(appPreferences)

    @Provides
    @Singleton
    fun provideWeatherDecoder(): WeatherDecoder = WeatherDecoder()
}