package com.production.auctionapplication.ui.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerAccountData
import com.production.auctionapplication.repository.database.OfficerDatabase
import kotlinx.coroutines.*
import timber.log.Timber

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Define a viewModel job
     */
    private var viewModelJob = Job()

    /**
     * Define a coroutine scope
     */
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Get Current user data from the repository.
     */
    val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    private var _currentOfficerData = MutableLiveData<OfficerAccountData>()
    val currentOfficerData: LiveData<OfficerAccountData>
        get() = _currentOfficerData

    init {
        checkOfficerData()
    }

    private fun checkOfficerData() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {

                try {
                    val currentValue =
                        repository.getAccountData()
                    _currentOfficerData.postValue(currentValue)

                    Timber.i(_currentOfficerData.toString())
                } catch (e: Exception) {
                    Timber.e(e.message.toString())
                }

            }
        }
    }

}