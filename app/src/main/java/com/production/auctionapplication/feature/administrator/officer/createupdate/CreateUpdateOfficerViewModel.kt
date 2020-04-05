package com.production.auctionapplication.feature.administrator.officer.createupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.auctionapplication.repository.networking.AuctionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateOfficerViewModel : ViewModel() {

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
            "Administrator" -> 1
            "Officer" -> 2
            else -> 3
        }
    }
}
