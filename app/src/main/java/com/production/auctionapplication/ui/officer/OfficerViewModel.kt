package com.production.auctionapplication.ui.officer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.production.auctionapplication.networking.AuctionApi
import com.production.auctionapplication.networking.Officer
import kotlinx.coroutines.*
import timber.log.Timber

class OfficerViewModel : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)

    // Encapsulation
    private val _officer = MutableLiveData<List<Officer>>()
    val officer: LiveData<List<Officer>>
        get() = _officer

    init {
        getAllOfficer()
    }

    private fun getAllOfficer() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {

                val getOfficerData =
                    AuctionApi.retrofitService.getAllOfficerAsync()

                try {
                    val result = getOfficerData.await()
                    _officer.postValue(result.officer)
                    Timber.i(_officer.value.toString())
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
