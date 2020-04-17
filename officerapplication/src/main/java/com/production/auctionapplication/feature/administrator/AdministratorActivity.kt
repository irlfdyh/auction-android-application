package com.production.auctionapplication.feature.administrator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.ActivityAdministratorBinding

class AdministratorActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityAdministratorBinding>(
                this, R.layout.activity_administrator)

//        setupNavigationDrawer()
        setSupportActionBar(binding.adminToolbar)

        val navController = this.findNavController(R.id.nav_host_fragment)

        drawerLayout = binding.adminDrawerLayout

        // displaying navigation drawer (burger icon).
        setupWithNavController(binding.navView, navController)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
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
