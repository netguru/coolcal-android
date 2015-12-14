package co.netguru.android.coolcal.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import co.netguru.android.coolcal.R
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.UpdateManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        setSupportActionBar(toolbar)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.setDisplayShowTitleEnabled(false)

        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        checkForCrashes()
    }

    private fun checkForCrashes() {
        CrashManager.register(this, getString(R.string.appIdHockeyApp));
    }

    private fun checkForUpdates() {
        // TODO Remove this for store / production builds!
        UpdateManager.register(this, getString(R.string.appIdHockeyApp));
    }
}
