package co.netguru.android.coolcal.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideAppPreferences(sharedPreferences: SharedPreferences, locale: Locale) =
            AppPreferences(sharedPreferences, locale)
}