package co.netguru.android.coolcal.utils

const val SYNC_INTERVAL = 30 * 60 * 1000L // 30 minutes

fun updateNeeded(syncTime: Long): Boolean {
    return System.currentTimeMillis() - syncTime > SYNC_INTERVAL
}