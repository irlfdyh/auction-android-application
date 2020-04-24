package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.util.REQUEST_CREATE_DATA_FAILED
import com.production.auctionapplication.util.REQUEST_CREATE_DATA_SUCCESS
import com.production.auctionapplication.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateStuffCategoryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Get reference to the Repository
     */
    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    /**
     * Use data binding to get or set text from edit text
     */
    var categoryName = MutableLiveData<String>()
    var categoryDescription = MutableLiveData<String>()

    /**
     * Handle some event that the event will be started
     * when this properties value is true
     */
    private var _clickState = MutableLiveData<Event<Boolean>>()
    val clickState: LiveData<Event<Boolean>>
        get() = _clickState

    /**
     * Enable or disable button accordingly this value.
     */
    private var _buttonEnable = MutableLiveData<Boolean>()
    val buttonEnable: LiveData<Boolean>
        get() = _buttonEnable

    /**
     * when this property value is true, fragment will start navigate.
     */
    private var _uploadIsSuccess = MutableLiveData<Event<Boolean>>()
    val uploadIsSuccess: LiveData<Event<Boolean>>
        get() = _uploadIsSuccess

    private var _showDialog = MutableLiveData<Event<Boolean>>()
    val showDialog: LiveData<Event<Boolean>>
        get() = _showDialog

    /**
     * Get any message from request or error message from exception
     */
    private var _uploadCode = MutableLiveData<Event<Int>>()

    private var _responseMessage = MutableLiveData<String>()

    val uploadMessage: LiveData<String> = Transformations.map(_uploadCode) { code ->
        when (code) {
            Event(REQUEST_CREATE_DATA_FAILED) -> application.getString(R.string.requset_failed_message)
            Event(REQUEST_CREATE_DATA_SUCCESS) -> _responseMessage.value.toString()
            else -> null
        }
    }

    private var isNewCategory: Boolean = false

    /**
     * This method is used to check, whether this is new data or not
     */
    fun onStart(categoryData: CategoryResponse?) {
        if (categoryData == null) {
            isNewCategory = true
        } else {
            categoryName.value = categoryData.categoryName
            categoryDescription.value = categoryData.categoryDescription

            Timber.i(categoryName.value)
            Timber.i(categoryDescription.value)
        }
    }

    fun uploadState(categoryId: String?, name: String, description: String) {

        // initial this property value
        _uploadIsSuccess.value = Event(false)

        if (isNewCategory || categoryId.isNullOrEmpty()) {
            onCreateData(name, description)
        } else {
            onUpdateData(categoryId, name, description)
        }
    }

    private fun onCreateData(name: String, description: String) {
        viewModelScope.launch {

            _showDialog.value = Event(true)
            _buttonEnable.value = false

            withContext(Dispatchers.IO) {

                val getCreationResponse =
                    AuctionApi.retrofitService
                        .createNewCategoryAsync(
                            getOfficerToken()!!, name, description, null)

                Timber.i("Request Started with value :  $name, $description")

                try {
                    val response = getCreationResponse.await()
                    Timber.i(response.toString())

                    if (response.categoryData != null) {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _uploadCode.postValue(Event(REQUEST_CREATE_DATA_SUCCESS))
                        _uploadIsSuccess.postValue(Event(true))
                    } else {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadIsSuccess.postValue(Event(false))

                    }
                } catch (e: Exception) {
                    Timber.i("onException : ${e.message.toString()}")

                    _showDialog.postValue(Event(false))
                    _buttonEnable.postValue(true)
                    _uploadCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                    _uploadIsSuccess.postValue(Event(false))
                }
            }
        }
    }

    private fun onUpdateData(categoryId: String, name: String, description: String) {
        viewModelScope.launch {

            _showDialog.value = Event(true)
            _buttonEnable.value = false

            withContext(Dispatchers.IO) {

                val getAction =
                    AuctionApi.retrofitService
                        .updateCategoryAsync(
                            categoryId,
                            getOfficerToken(),
                            name, description
                        )

                Timber.i("Request Started with value :  $categoryId, $name, $description")

                try {
                    val response = getAction.await()
                    Timber.i(response.toString())

                    if (response.categoryData != null) {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _uploadCode.postValue(Event(REQUEST_CREATE_DATA_SUCCESS))
                        _uploadIsSuccess.postValue(Event(true))
                    } else {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadIsSuccess.postValue(Event(false))
                    }
                } catch (e: Exception) {
                    Timber.e("onException : ${e.message.toString()}")

                    _showDialog.postValue(Event(false))
                    _buttonEnable.postValue(true)
                    _uploadCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                    _uploadIsSuccess.postValue(Event(false))
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

    fun onClickAction() {
        _clickState.value = Event(true)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}