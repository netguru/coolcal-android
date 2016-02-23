package co.netguru.android.coolcal.utils

import android.util.Log

@Suppress("unused")
inline fun <reified T> T.logDebug(stringExpr: () -> String?) {
    val tag = tag<T>()
    Log.d(tag, stringExpr.invoke() ?: "")
}

@Suppress("unused")
inline fun <reified T> T.logError(stringExpr: () -> String?) {
    val tag = tag<T>()
    Log.e(tag, stringExpr.invoke() ?: "")
}

inline fun <reified T> tag() = T::class.javaClass.simpleName