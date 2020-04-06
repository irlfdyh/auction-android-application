package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateStuffCategoryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Use data binding to get or set text from edit text
     */
    val categoryName = MutableLiveData<String>()
    val categoryDescription = MutableLiveData<String>()

    /**
     * Handle some event that the event will be started
     * when this properties value is true
     */
    private var _clickState = MutableLiveData<Boolean>()
    val clickState: LiveData<Boolean>
        get() = _clickState

    /**
     * use to observe are the data creation is success
     * or not, to triggered some event.
     */
    private var _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    /**
     * Get reference to the Repository
     */
    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    fun onSaveNewData(name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val getCreationResponse =
                    AuctionApi.retrofitService
                        .createNewCategoryAsync(
                            getOfficerToken()!!, name, description)

                Timber.i("Request Started with value :  $name, $description")

                try {
                    val response = getCreationResponse.await()
                    Timber.i(response.message)
                    createSuccess()
                } catch (e: Exception) {
                    Timber.i(e.message.toString())
                }
            }
        }
    }

    /**
     * the request parameter is need token to verify the user, so this function
     * is used to get the officer token from the database.
     */
    private suspend fun getOfficerToken(): String? {
        return withContext(Dispatchers.IO) {
            // get user data from repository
            val data = repository.getAccountData()
            // just returning user token
            data?.token
        }
    }

    fun buttonClick() {
        _clickState.value = true
    }

    fun restartClickState() {
        if (_clickState.value == true) {
            _clickState.value = false
        }
    }

    private fun createSuccess() {
        _createSuccess.postValue(true)
    }

    fun restartCreationState() {
        if (_createSuccess.value == true) {
            _createSuccess.value = false
        }
    }

}
