package co.netguru.android.coolcal.utils

import android.content.SharedPreferences
import android.util.Log
import co.netguru.android.coolcal.rest.RestModule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Suppress("unused")
inline fun <reified T : Any> T.logDebug(msg: String?) {
    val tag = T::class.java.simpleName
    Log.d(tag, msg ?: "")
}

@Suppress("unused")
inline fun <reified T : Any> T.logError(msg: String?) {
    val tag = T::class.java.simpleName
    Log.e(tag, msg ?: "")
}


inline fun into(preferences: SharedPreferences,
                crossinline block: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
    preferences.edit().block().apply()
}