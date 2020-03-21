package com.production.auctionapplication.ui.stuffcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.production.auctionapplication.networking.AuctionApi
import com.production.auctionapplication.networking.Category
import kotlinx.coroutines.*
import timber.log.Timber

class StuffCategoryViewModel : ViewModel() {

    // Coroutine Job
    private var viewModelJob = Job()
    // Coroutine Scope (use main Dispatchers because this scope is
    // affected to the UI)
    private val coroutineScope = CoroutineScope(viewModelJob+Dispatchers.Main)

    // Encapsulation data
    private val _stuffCategory = MutableLiveData<List<Category>>()
    val stuffCategory: LiveData<List<Category>>
        get() = _stuffCategory

    init {
        getAllStuffCategory()
    }

    /**
     * Get all stuff category data
     */
    private fun getAllStuffCategory() {
        // launch in the background
        coroutineScope.launch {
            withContext(Dispatchers.IO) {

                val getStuffCategory =
                    AuctionApi.retrofitService.getAllCategoryAsync()

                try {
                    val listResult = getStuffCategory.await()
                    _stuffCategory.postValue(listResult.categoryData)
                    Timber.i( _stuffCategory.value.toString())
                } catch (e: Exception) {
                    // Set the list value to empty
                    _stuffCategory.value = ArrayList()
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
