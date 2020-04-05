package com.production.auctionapplication.feature.stuff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.Stuff
import kotlinx.coroutines.*
import timber.log.Timber

class StuffViewModel : ViewModel() {

    private var viewModelJob = Job()

    /**
     * Coroutine scope for a new Job using Main Dispatcher, because
     * this is affected to the UI.
     */
    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)

    // Encapsulation the variable
    private val _stuff = MutableLiveData<List<Stuff>>()
    val stuff: LiveData<List<Stuff>>
        get() = _stuff

    /**
     * Used for trigger some event from this properties value
     */
    private val _clickHandler = MutableLiveData<Boolean>()
    val clickHandler: LiveData<Boolean>
        get() = _clickHandler

    init {
        getAllStuffData()
    }

    /**
     * Getting all stuff data from the API
     */
    private fun getAllStuffData() {
        // launching the coroutine
        coroutineScope.launch {
            // switch to the I/O thread
            withContext(Dispatchers.IO) {
                val getStuffDeferred =
                    AuctionApi.retrofitService.getAllStuffAsync()
                try {
                    val result  = getStuffDeferred.await()
                    _stuff.postValue(result.stuffData)
                    Timber.i(_stuff.toString())
                } catch (e: Exception) {
                    Timber.e(e.message.toString())
                }
            }
        }
    }

    fun onButtonClick() {
        _clickHandler.value = true
    }

    fun restartClickState() {
        if (_clickHandler.value == true) {
            _clickHandler.value = false
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
