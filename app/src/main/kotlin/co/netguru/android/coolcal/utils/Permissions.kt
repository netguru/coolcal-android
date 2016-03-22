package co.netguru.android.coolcal.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import co.netguru.android.coolcal.BuildConfig

fun askForPermission(activity: Activity, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(activity,
            arrayOf(permission),
            requestCode)
}

fun isPermissionGranted(activity: Activity, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(activity,
            permission)
            == PackageManager.PERMISSION_GRANTED)
}

inline fun <T : Activity> T.givenPermission(permission: String,
                                            requestCode: Int,
                                            crossinline block: () -> Unit): Unit {
    if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.M) {
        if (isPermissionGranted(this, permission)){
            block()
        } else {
            askForPermission(this, permission, requestCode)
        }
    } else {
        block()
    }
}