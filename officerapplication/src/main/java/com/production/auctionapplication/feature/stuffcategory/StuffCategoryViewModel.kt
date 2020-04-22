package com.production.auctionapplication.feature.stuffcategory

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.util.ERROR_CTO
import com.production.auctionapplication.util.ERROR_NO_RESPONSE
import com.production.auctionapplication.util.Event
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

class StuffCategoryViewModel(application: Application) : AndroidViewModel(application) {

    // Encapsulation data
    private val _stuffCategory = MutableLiveData<List<CategoryResponse>>()
    val stuffCategory: LiveData<List<CategoryResponse>>
        get() = _stuffCategory

    /**
    * to set the view state (e.g visibility, enable, etc)
    */
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

    private var _clickState = MutableLiveData<Event<Boolean>>()
    val clickState: LiveData<Event<Boolean>>
        get() = _clickState

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
        getAllStuffCategory()
    }

    /**
     * Get all stuff category data
     */
    fun getAllStuffCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _isLoading.postValue(true)
                _isDataEmpty.postValue(false)
                _isLoadDataFailed.postValue(false)
                _isRequestSuccess.postValue(false)

                val getStuffCategory =
                    AuctionApi.retrofitService.getAllCategoryAsync()

                try {
                    val result = getStuffCategory.await()
                    if (result.categoryData.isEmpty()) {
                        _isDataEmpty.postValue(true)
                        _isLoading.postValue(false)
                        _isRequestSuccess.postValue(true)
                    } else {
                        _stuffCategory.postValue(result.categoryData)
                        _isLoading.postValue(false)
                        _isRequestSuccess.postValue(true)
                        Timber.i( _stuffCategory.value.toString())
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

    fun clickAction() {
        _clickState.value = Event(true)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}
