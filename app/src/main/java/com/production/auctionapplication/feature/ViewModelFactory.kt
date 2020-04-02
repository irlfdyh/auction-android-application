package com.production.auctionapplication.feature

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.feature.signin.SigninViewModel
import com.production.auctionapplication.feature.splashscreen.SplashViewModel
import com.production.auctionapplication.feature.stuffcategory.createupdate.CreateUpdateStuffCategoryViewModel

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                return SigninViewModel(
                    application
                ) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                return SplashViewModel(application) as T
            }
            modelClass.isAssignableFrom(CreateUpdateStuffCategoryViewModel::class.java) -> {
                return CreateUpdateStuffCategoryViewModel(application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}