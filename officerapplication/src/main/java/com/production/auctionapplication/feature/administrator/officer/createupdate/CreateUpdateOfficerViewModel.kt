package com.production.auctionapplication.feature.administrator.officer.createupdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.auctionapplication.repository.networking.AuctionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateOfficerViewModel : ViewModel() {

    private var _clickState = MutableLiveData<Boolean>()
    val clickState: LiveData<Boolean>
        get() = _clickState

    private var _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    fun onNewOfficerData(
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
                            username,
                            password,
                            name,
                            phone,
                            status
                        )

                try {
                    val getResponse = createData.await()
                    Timber.i(getResponse.toString())
                    createSuccess()
                } catch (e: Exception) {
                    Timber.e(e.message.toString())
                }
            }
        }

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

    fun buttonClick() {
        _clickState.value = true
        Timber.i("Button Clicked")
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
