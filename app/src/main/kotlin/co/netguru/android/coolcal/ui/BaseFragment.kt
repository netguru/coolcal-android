package co.netguru.android.coolcal.ui

import android.location.Location
import android.support.v4.app.Fragment
import com.google.android.gms.location.LocationListener
import rx.Subscription

abstract class BaseFragment : Fragment(), LocationListener {

    protected var subscription: Subscription? = null
    @Volatile protected var currentLocation: Location? = null

    override fun onLocationChanged(location: Location?) {
        currentLocation = location
    }

    override fun onDestroy() {
        // this is to prevent mem leaks
        subscription?.unsubscribe()
        super.onDestroy()
    }
}