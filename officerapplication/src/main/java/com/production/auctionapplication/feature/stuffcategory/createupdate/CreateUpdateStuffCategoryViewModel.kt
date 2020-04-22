package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.DetailCategoryResponse
import com.production.auctionapplication.repository.networking.models.category.RequestDetailCategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateStuffCategoryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Use data binding to get or set text from edit text
     */
    var categoryName = MutableLiveData<String>()
    var categoryDescription = MutableLiveData<String>()

    private var _detailResponse = MutableLiveData<DetailCategoryResponse>()
    val detailResponse: LiveData<DetailCategoryResponse> = _detailResponse

    /**
     * Handle some event that the event will be started
     * when this properties value is true
     */
    private var _clickState = MutableLiveData<Boolean>()
    val clickState: LiveData<Boolean>
        get() = _clickState

    /**
     * use to observe are the data creation is success
     * or not, to triggered some event.
     */
    private var _uploadState = MutableLiveData<Boolean>()
    val uploadState: LiveData<Boolean>
        get() = _uploadState

    /**
     * Get reference to the Repository
     */
    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    private var isNewCategory: Boolean = false

    /**
     * This method is used to check, whether this is new data or not
     */
    fun onStart(categoryId: String?) {
        if (categoryId == null) {
            isNewCategory = true
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                    val getCategoryData =
                        AuctionApi.retrofitService
                            .getDetailCategoryAsync(categoryId)
                    try {
                        val response = getCategoryData.await()
                        if (response.message.isNotEmpty()) {
                            onDataLoaded(response.categoryData)
                        }
                    } catch (e: Exception) {
                        Timber.e(e.message.toString())
                    }
                }
            }
        }
    }

    private fun onDataLoaded(data: DetailCategoryResponse) {
        categoryName.postValue(data.categoryName)
        categoryDescription.postValue(data.categoryDescription)

        _detailResponse.postValue(data)

        Timber.i("Detail response ${_detailResponse.value.toString()}")
        Timber.i("Name Value ${categoryName.value.toString()}")
        Timber.i("Desc Value ${categoryDescription.value.toString()}")
        Timber.i("Main response ${data.toString()}")
    }

    fun uploadData(categoryId: String?, name: String, description: String) {
        if (isNewCategory || categoryId.isNullOrEmpty()) {
            onCreateData(name, description)
        } else {
            onUpdateData(categoryId, name, description)
        }
    }

    private fun onCreateData(name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val getCreationResponse =
                    AuctionApi.retrofitService
                        .createNewCategoryAsync(
                            getOfficerToken()!!, name, description)

                Timber.i("Request Started with value :  $name, $description")

                try {
                    val response = getCreationResponse.await()
                    Timber.i(response.message)
                    uploadSuccess()
                } catch (e: Exception) {
                    Timber.i(e.message.toString())
                }
            }
        }
    }

    private fun onUpdateData(categoryId: String, name: String, description: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val getAction =
                    AuctionApi.retrofitService
                        .updateCategoryAsync(
                            categoryId,
                            getOfficerToken()!!,
                            name, description
                        )

                Timber.i("Request Started with value :  $categoryId, $name, $description")

                try {
                    val response = getAction.await()
                    Timber.i(response.toString())
                    uploadSuccess()
                } catch (e: Exception) {
                    Timber.e("Exc error ${e.message.toString()}")
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

    fun buttonClick() {
        _clickState.value = true
    }

    fun restartClickState() {
        if (_clickState.value == true) {
            _clickState.value = false
        }
    }

    private fun uploadSuccess() {
        _uploadState.postValue(true)
    }

    fun restartCreationState() {
        if (_uploadState.value == true) {
            _uploadState.value = false
        }
    }

}
