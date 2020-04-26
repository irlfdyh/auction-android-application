package com.production.auctionapplication.feature.administrator.officer.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.officer.OfficerResponse
import com.production.auctionapplication.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateOfficerViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Bind xml view data
     */
    val officerStatusList = listOf("Active", "Inactive")
    val officerLevelList = listOf("Admin", "Petugas")

    var officerUsername = MutableLiveData<String>()
    var officerPassword = MutableLiveData<String>()
    var officerName = MutableLiveData<String>()
    var officerPhone = MutableLiveData<String>()
    var officerStatus = MutableLiveData<String>()
    var officerLevel = MutableLiveData<String>()

    private var _clickState = MutableLiveData<Event<Boolean>>()
    val clickState: LiveData<Event<Boolean>>
        get() = _clickState

    private var _uploadSuccess = MutableLiveData<Event<Boolean>>()
    val uploadSuccess: LiveData<Event<Boolean>>
        get() = _uploadSuccess

    private var _buttonEnable = MutableLiveData<Boolean>()
    val buttonEnable: LiveData<Boolean>
        get() = _buttonEnable

    private var _showDialog = MutableLiveData<Event<Boolean>>()
    val showDialog: LiveData<Event<Boolean>>
        get() = _showDialog

    private var _hideViewGroup = MutableLiveData<Boolean>()
    val hideViewGroup: LiveData<Boolean>
        get() = _hideViewGroup

    /**
     * Get any message from request or error message from exception
     */
    private var _uploadStateCode = MutableLiveData<Event<Int>>()

    private var _responseMessage = MutableLiveData<String>()

    val uploadMessage: LiveData<String> = Transformations.map(_uploadStateCode) { code ->
        when (code) {
            Event(REQUEST_CREATE_DATA_FAILED) -> application.getString(R.string.requset_failed_message)
            Event(REQUEST_CREATE_DATA_SUCCESS) -> _responseMessage.value.toString()
            else -> null
        }
    }

    /**
     *  Setup default value for new or old data is false
     */
    private var isNewData: Boolean = false
    private var officerId =  MutableLiveData<String>()
    private var buttonTextCode = MutableLiveData<Int>()

    var buttonText = Transformations.map(buttonTextCode) {
        when (it) {
            BUTTON_CREATE_CODE -> application.getString(R.string.create_text)
            else -> application.getString(R.string.update_text)
        }
    }

    fun onStart(officerData: OfficerResponse?) {

        // initialize button enable to true
        _buttonEnable.value = true
        // initialize stuffId value is present
        officerId.value = officerData?.officerId.toString()

        if (officerData == null) {
            isNewData = true

            buttonTextCode.value = BUTTON_CREATE_CODE
        } else {
            officerName.value = officerData.officerName
            buttonTextCode.value = BUTTON_UPDATE_CODE
        }

    }

    fun onPrepareUploadData(
        level: String,
        username: String,
        password: String,
        name: String,
        phone: String,
        status: String
    ) {

        // initial this property value
        _uploadSuccess.value = Event(false)
        _showDialog.value = Event(true)
        _buttonEnable.value = false

        if (isNewData || officerId.value.isNullOrEmpty()) {
            onCreateData(level, username, password, name, phone, status)
        } else {
            onUpdateData()
        }

    }

    private fun onCreateData(
        level: String,
        username: String,
        password: String,
        name: String,
        phone: String,
        status: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val createData =
                    AuctionApi.retrofitService
                        .createNewOfficerAsync(
                            levelConverter(level),
                            username, password,
                            name, phone, status
                        )

                try {
                    val response = createData.await()
                    Timber.i(response.toString())

                    if (response.officerData != null) {

                        _showDialog.postValue(Event(false))
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_SUCCESS))
                        _uploadSuccess.postValue(Event(true))
                    } else {

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadSuccess.postValue(Event(false))
                    }
                } catch (e: Exception) {
                    Timber.e(e.message.toString())

                    _showDialog.postValue(Event(false))
                    _buttonEnable.postValue(true)
                    _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                    _uploadSuccess.postValue(Event(false))
                }
            }
        }
    }

    private fun onUpdateData() {
        //TODO: Update data action here
    }

    /**
     * this function is used for convert the level value from string to Integer
     */
    private fun levelConverter(levelString: String): Int {
        return when (levelString) {
            "Admin" -> 1
            "Petugas" -> 2
            else -> 3
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
