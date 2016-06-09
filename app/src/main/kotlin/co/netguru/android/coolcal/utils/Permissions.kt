package co.netguru.android.coolcal.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import co.netguru.android.coolcal.BuildConfig

fun askForPermission(activity: Activity, permission: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(activity,
            permission,
            requestCode)
}

fun isPermissionGranted(activity: Activity, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(activity,
            permission)
            == PackageManager.PERMISSION_GRANTED)
}

fun arePermissionsGranted(activity: Activity, permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (!isPermissionGranted(activity, permission)) {
            return false
        }
    }
    return true
}

inline fun <T : Activity> T.givenPermission(permissions: Array<String>,
                                            requestCode: Int,
                                            crossinline block: () -> Unit): Unit {
    if (BuildConfig.VERSION_CODE >= Build.VERSION_CODES.M) {
        if (arePermissionsGranted(this, permissions)) {
            block()
        } else {
            askForPermission(this, permissions, requestCode)
        }
    } else {
        block()
    }
}

inline fun <T : Activity> T.ifPermissionsGranted(permissions: Array<String>
                                                 , crossinline onPermissionsGranted: () -> Unit) {
    if (arePermissionsGranted(this, permissions)) {
        onPermissionsGranted()
    }
}