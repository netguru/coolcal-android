package co.netguru.android.coolcal.app

import android.location.Location
import android.support.v4.app.Fragment
import com.google.android.gms.location.LocationListener
import rx.Subscription

abstract class BaseFragment : Fragment(), LocationListener {

    protected var subscription: Subscription? = null
    protected var currentLocation: Location? = null

    override fun onLocationChanged(location: Location?) {
        currentLocation = location
    }

    override fun onDestroy() {
        // this is to prevent mem leaks
        subscription?.unsubscribe()
        super.onDestroy()
    }
}