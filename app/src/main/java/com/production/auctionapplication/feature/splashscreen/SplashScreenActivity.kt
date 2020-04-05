package com.production.auctionapplication.feature.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.R
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.feature.administrator.AdministratorActivity
import com.production.auctionapplication.feature.officer.OfficerMainActivity
import com.production.auctionapplication.feature.signin.SigninActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val application = requireNotNull(this).application

        // Creating instance of the viewModelFactory.
        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SplashViewModel::class.java)

        viewModel.currentOfficerData.observe(this, Observer {
            when (it?.levelId) {
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
