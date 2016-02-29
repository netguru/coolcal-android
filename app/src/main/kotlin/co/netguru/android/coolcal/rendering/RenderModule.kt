package co.netguru.android.coolcal.rendering

import android.content.Context
import android.graphics.Bitmap
import co.netguru.android.coolcal.preferences.AppPreferences
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*
import javax.inject.Singleton

@Module
class RenderModule {

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
    fun provideTimeFormatter(context: Context, locale: Locale,
                             periodFormatter: PeriodFormatter): TimeFormatter =
            TimeFormatterImpl(context, locale, periodFormatter)

    @Provides
    @Singleton
    fun provideWeatherDataFormatter(appPreferences: AppPreferences): WeatherDataFormatter =
            WeatherDataFormatterImpl(appPreferences)

    @Provides
    @Singleton
    fun provideWeatherDecoder(): WeatherDecoder = WeatherDecoder()

    @Provides
    @Singleton
    fun providePicasso(context: Context): Picasso = Picasso.Builder(context)
            .defaultBitmapConfig(Bitmap.Config.RGB_565)
            .build()

}