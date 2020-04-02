package com.production.auctionapplication.feature.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.ActivityAdministratorBinding

class AdministratorActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityAdministratorBinding>(
                this, R.layout.activity_administrator)

        setupNavigationDrawer()
        setSupportActionBar(binding.adminToolbar)

        val navController = this.findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.stuffFragment, R.id.officerFragment, R.id.stuffCategoryFragment
            ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        // To displaying navigation drawer
        setupWithNavController(binding.navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    /**
     * Set up the drawer layout, with applying the status bar color.
     */
    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.admin_drawer_layout))
            .apply {
                setStatusBarBackground(R.color.colorPrimaryDark)
            }
    }

}
