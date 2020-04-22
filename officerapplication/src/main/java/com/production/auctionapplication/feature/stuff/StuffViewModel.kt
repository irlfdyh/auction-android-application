package com.production.auctionapplication.feature.stuff

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.stuff.StuffResponse
import com.production.auctionapplication.util.ERROR_CTO
import com.production.auctionapplication.util.ERROR_NO_RESPONSE
import com.production.auctionapplication.util.Event
import kotlinx.coroutines.*
import timber.log.Timber

class StuffViewModel(application: Application) : AndroidViewModel(application) {

    // Encapsulation the variable
    private val _stuff = MutableLiveData<List<StuffResponse?>>()
    val stuff: LiveData<List<StuffResponse?>>
        get() = _stuff

    /**
     * Used for trigger some event from this properties value
     */
    private val _clickHandler = MutableLiveData<Event<Boolean>>()
    val clickHandler: LiveData<Event<Boolean>>
        get() = _clickHandler

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _isDataEmpty = MutableLiveData<Boolean>()
    val isDataEmpty: LiveData<Boolean>
        get() = _isDataEmpty

    private var _isLoadDataFailed = MutableLiveData<Boolean>()
    val isLoadDataFailed: LiveData<Boolean>
        get() = _isLoadDataFailed

    private var _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean>
        get() = _isRequestSuccess

    private var _errorCode = MutableLiveData<Int>()

    /**
     * setup error message according to the error message
     */
    val errorMessage: LiveData<String> = Transformations.map(_errorCode) {
        when (it) {
            ERROR_CTO -> application.getString(R.string.cto_text)
            else -> application.getString(R.string.no_connection_text)
        }
    }

    init {
        getAllStuffData()
    }

    /**
     * Getting all stuff data from the API
     */
    fun getAllStuffData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _isLoading.postValue(true)
                _isDataEmpty.postValue(false)
                _isLoadDataFailed.postValue(false)
                _isRequestSuccess.postValue(false)

                val getStuffDeferred =
                    AuctionApi.retrofitService.getAllStuffAsync()

                try {
                    val result  = getStuffDeferred.await()
                    if (result.stuffData.isEmpty()) {
                        _isDataEmpty.postValue(true)
                        _isLoading.postValue(false)
                        _isRequestSuccess.postValue(true)
                    } else {
                        _stuff.postValue(result.stuffData)
                        _isLoading.postValue(false)
                        _isRequestSuccess.postValue(true)
                    }
                } catch (e: Exception) {
                    Timber.e(e.message.toString())

                    _isLoadDataFailed.postValue(true)
                    _isLoading.postValue(false)
                    _isRequestSuccess.postValue(false)

                    // setup error message
                    when (e.message.toString()) {
                        "timeout" -> _errorCode.postValue(ERROR_CTO)
                        else -> _errorCode.postValue(ERROR_NO_RESPONSE)
                    }
                }
            }
        }
    }

    fun onButtonClick() {
        _clickHandler.value = Event(true)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
