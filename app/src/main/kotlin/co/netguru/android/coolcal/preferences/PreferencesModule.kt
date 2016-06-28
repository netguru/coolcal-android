package co.netguru.android.coolcal.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideAppPreferences(sharedPreferences: SharedPreferences, locale: Locale, gson: Gson) =
            AppPreferences(sharedPreferences, locale, gson)
}