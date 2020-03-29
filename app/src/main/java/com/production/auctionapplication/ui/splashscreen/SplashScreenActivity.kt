package com.production.auctionapplication.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.R
import com.production.auctionapplication.ui.administrator.AdministratorActivity
import com.production.auctionapplication.ui.officer.OfficerMainActivity
import com.production.auctionapplication.ui.signin.SigninActivity
import com.production.auctionapplication.ui.signin.SigninViewModelFactory
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val application = requireNotNull(this).application

        // Creating instance of the viewModelFactory.
        val viewModelFactory = SigninViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SplashViewModel::class.java)

        viewModel.currentOfficerData.observe(this, Observer {
            when (it.levelId) {
                1 -> {
                    val intent = Intent(this, AdministratorActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this, OfficerMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

    }
}
