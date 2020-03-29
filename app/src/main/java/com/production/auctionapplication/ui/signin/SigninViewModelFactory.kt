package com.production.auctionapplication.ui.signin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.repository.database.OfficerDatabaseDao
import com.production.auctionapplication.ui.splashscreen.SplashViewModel

class SigninViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SigninViewModel::class.java)) {
            return SigninViewModel(application) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}