package co.netguru.android.coolcal.ui

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import co.netguru.android.coolcal.BuildConfig
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.utils.givenPermission
import co.netguru.android.coolcal.utils.ifPermissionsGranted
import co.netguru.android.coolcal.utils.logDebug
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import de.psdev.licensesdialog.LicensesDialog
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.CrashManager

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val REQUEST_RESOLVE_ERROR = 1000
        const val PERMISSIONS_REQUEST_CALENDAR_LOCATION = 1
    }

    private var mResolvingError: Boolean = false

    private val googleApiClient: GoogleApiClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private val fragments: List<BaseFragment?> by lazy {
        arrayListOf(supportFragmentManager
                .findFragmentById(R.id.weather_fragment) as BaseFragment?,
                supportFragmentManager
                        .findFragmentById(R.id.events_fragment) as BaseFragment?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.showOverflowMenu()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        slidingLayout.setPanelSlideListener(fragments[1] as SlidingUpPanelLayout.PanelSlideListener)

        givenPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALENDAR), PERMISSIONS_REQUEST_CALENDAR_LOCATION, {
        })
        checkForCrashes()
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onResume() {
        super.onResume()
        ifPermissionsGranted(arrayOf(Manifest.permission.READ_CALENDAR), {
            val fragment = fragments[1] as EventsFragment
            fragment.onCalendarPermissionGranted()
        })
    }

    override fun onStop() {
        super.onStop()
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_licenses -> {
                displayLicenses()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun displayLicenses() {
        LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .build()
                .showAppCompat();
    }

    private fun checkForCrashes() {
        CrashManager.register(this, BuildConfig.HOCKEY_APP_ID)
    }

    /*
        Google API callbacks
     */
    private fun showErrorDialog(errorCode: Int) {
        val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, errorCode, REQUEST_RESOLVE_ERROR)
        dialog.setOnDismissListener({ mResolvingError = false })
        dialog.show()
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        logDebug("Connection Failed with code ${result.errorCode}")
        if (mResolvingError) {
            return
        }
        if (result.hasResolution()) {
            try {
                mResolvingError = true
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR)
            } catch (e: IntentSender.SendIntentException) {
                // todo: signal error to user
            }
        } else {
            showErrorDialog(result.errorCode)
            mResolvingError = true
        }
    }

    override fun onConnected(p0: Bundle?) {
        givenPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_CALENDAR_LOCATION) {
            getLocation()
        }
    }

    private fun getLocation() {
        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (location != null) {
            fragments.forEach { fragment -> fragment?.onLocationChanged(location) }
        } else {
            Snackbar.make(slidingLayout, applicationContext.resources.getString(R.string.localization_disabled), Snackbar.LENGTH_LONG).show()
            logDebug("Null location!")
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        googleApiClient.connect()
    }

    override fun onBackPressed() {
        when (slidingLayout.panelState) {
            SlidingUpPanelLayout.PanelState.EXPANDED,
            SlidingUpPanelLayout.PanelState.ANCHORED,
            SlidingUpPanelLayout.PanelState.DRAGGING -> {
                slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
            else -> super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun onCalendarPermissionResult(i: Int) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                val fragment = fragments[1] as EventsFragment
                fragment.onCalendarPermissionGranted()
            } else {
                // TODO action/placeholder on calendar permission denied??
                Snackbar.make(slidingLayout, applicationContext
                        .resources.getString(R.string.calendar_access_not_granted), Snackbar.LENGTH_LONG).show()
            }
        }

        fun onLocationPermissionResult(i: Int) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                googleApiClient.reconnect()
            } else {
                // TODO consider placeholder for lack of weather??
                Snackbar.make(slidingLayout, applicationContext.resources
                        .getString(R.string.localization_access_not_granted), Snackbar.LENGTH_LONG).show()
            }
        }

        when (requestCode) {
            PERMISSIONS_REQUEST_CALENDAR_LOCATION -> {
                if (grantResults.size > 0) {
                    for (i in 0..permissions.size - 1) {
                        when (permissions[i]) {
                            Manifest.permission.ACCESS_FINE_LOCATION -> onLocationPermissionResult(i)
                            Manifest.permission.READ_CALENDAR -> onCalendarPermissionResult(i)
                            else -> throw IllegalArgumentException("Not handled permission: ${permissions[i]}")
                        }
                    }
                }
            }
        }
    }
}
