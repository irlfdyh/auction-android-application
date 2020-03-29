package com.production.auctionapplication.ui.signin

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerAuth
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import kotlinx.coroutines.*
import timber.log.Timber

class SigninViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    /**
     * Setting the coroutine scope
     */
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Use this variable to check are the login is success, and handle
     * the login data for database.
     */
    private var _loginData = MutableLiveData<OfficerAuth>()
    val loginState: LiveData<OfficerAuth>
        get() = _loginData

    /**
     * for showing the snackbar
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    /**
     * use this two properties to check are the user is successfully logged in.
     */
    private val failedLogin: LiveData<Boolean> = Transformations.map(_loginData) {
        it.token.isNullOrEmpty()
    }
    private val successLogin: LiveData<Boolean> = Transformations.map(_loginData) {
        it.token?.isNotEmpty()
    }

    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    /**
     * function for handling user action at login activity.
     */
    fun onSigninAction(username: String, password: String) {
        coroutineScope.launch {
            // Work at IO thread for the better application performance
            withContext(Dispatchers.IO) {

                val getSigninResponse =
                    AuctionApi.retrofitService.officerSignInAsync(username, password)

                Timber.i("Request Started...")

                try {
                    // Getting request response
                    val response = getSigninResponse.await()

                    // get current response data
                    _loginData.postValue(response)

                    //
                    checkLoginStatus()

                    // insert response data to database according to the
                    // condition at this function
                    insertOfficerData(response)

                    // Logging
                    Timber.i(response.toString())
                } catch (e: Exception) {
                    Timber.e(e.message.toString(), "Exception")
                }
            }
        }
    }

    /**
     * Check officer login status, if the login is failed then show the snackbar.
     */
    private fun checkLoginStatus() {
        // Check are the user is failed to login
        if (_loginData.value?.token == null) {
            _showSnackbarEvent.postValue(true)
            Timber.i("snackbar event changed to true")
        }
    }

    /**
     * for set the property value to false again.
     */
    fun doneShowingSnackbar() {
        // check the properties value to handle the recur execute program
        if (_showSnackbarEvent.value == true) {
            _showSnackbarEvent.postValue(false)
            Timber.i("snackbar event changed to false")
        }
    }

    /**
     * Insert the officer data when login is success.
     */
    private suspend fun insertOfficerData(data: OfficerAuth) {
        withContext(Dispatchers.IO) {
            if (_loginData.value?.token != null) {
                repository.saveAccountData(data)

                Timber.i("Data Inserted to the Database")
            }
        }
    }


}