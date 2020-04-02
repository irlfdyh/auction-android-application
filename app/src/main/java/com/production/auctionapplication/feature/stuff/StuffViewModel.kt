package com.production.auctionapplication.feature.stuff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.Stuff
import kotlinx.coroutines.*
import timber.log.Timber

class StuffViewModel : ViewModel() {

    // Encapsulation the variable
    private val _stuff = MutableLiveData<List<Stuff>>()
    val stuff: LiveData<List<Stuff>>
        get() = _stuff

    // Create a Job
    private var viewModelJob = Job()

    /**
     * Coroutine scope for a new Job using Main Dispatcher, because
     * this is affected to the UI.
     */
    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
