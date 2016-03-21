package co.netguru.android.coolcal.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

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