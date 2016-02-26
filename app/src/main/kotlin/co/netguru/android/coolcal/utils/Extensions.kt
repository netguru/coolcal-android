package co.netguru.android.coolcal.utils

import android.content.SharedPreferences
import android.util.Log
import co.netguru.android.coolcal.BuildConfig

@Suppress("unused")
inline fun <reified T : Any> T.logDebug(stringExpr: () -> String?) {
    //    val tag = T::class.java.simpleName
    Log.d(BuildConfig.APPLICATION_ID, stringExpr.invoke() ?: "")
}

@Suppress("unused")
inline fun <reified T : Any> T.logError(stringExpr: () -> String?) {
    //    val tag = T::class.java.simpleName
    Log.e(BuildConfig.APPLICATION_ID, stringExpr.invoke() ?: "")
}


inline fun into(preferences: SharedPreferences,
                crossinline block: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
    preferences.edit().block().apply()
}