package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerAccountData
import com.production.auctionapplication.repository.database.OfficerAuth
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class CreateUpdateStuffCategoryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Use data binding to get or set text from edit text
     */
    val categoryName = MutableLiveData<String>()
    val categoryDescription = MutableLiveData<String>()

    private var categoryId: String? = null

    private var isNewData: Boolean = false

    private var isDataLoaded = false

    private var dataCreated = false

    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    /**
     * This properties used for handle snackbar message
     */
    private var _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String>
        get() = _snackbarText


    fun start() {

    }

    fun onSaveNewData(name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val getCreationResponse =
                    AuctionApi.retrofitService
                        .createNewCategoryAsync(
                            getOfficerToken()!!, name, description)

                Timber.i("Request Started with data :  ${getCreationResponse.await()}")

                try {
                    val response = getCreationResponse.await()
                    Timber.i(response.message)
                } catch (e: Exception) {
                    Timber.i(e.message.toString())
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

}
