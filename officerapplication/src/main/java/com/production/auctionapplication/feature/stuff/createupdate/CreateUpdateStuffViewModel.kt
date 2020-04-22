package com.production.auctionapplication.feature.stuff.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.repository.networking.models.category.RequestAllCategoryResponse
import com.production.auctionapplication.repository.networking.models.category.getCategoryName
import com.production.auctionapplication.util.currentTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.toString as toString1

class CreateUpdateStuffViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * properties is used to hold data from response
     */
    private var _stuffCategory = mutableListOf<CategoryResponse>()

    /**
     * properties to setup key and value to get id from item that selected
     * from the drop down.
     */
    private val setKeyValue = mutableMapOf<String?, CategoryResponse>()

    /**
     * to get the [categoryId] from the input key which it is the same item
     */
     private fun setupCategoryId(key: String): Int {
        val categoryId =
            _stuffCategory.associateByTo(setKeyValue) {
                it.categoryName
            }
        return categoryId[key]?.categoryId!!.toInt()
    }

    /**
     * Properties to hold the category name
     */
    private var _categoryName = MutableLiveData<List<String?>>()
    val categoryName: LiveData<List<String?>>
        get () = _categoryName

    /**
     * to handle the click button with triggered events.
     */
    private var _clickState = MutableLiveData<Boolean>()
    val clickState: LiveData<Boolean>
        get() = _clickState

    /**
     * reference to get the user token from the local database.
     */
    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    /**
     * to trigger navigation to the stuff list fragment.
     */
    private var _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    /**
     * Get stuff category from the API.
     */
    fun getStuffCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val getCategory =
                    AuctionApi.retrofitService.getAllCategoryAsync()

                try {
                    val result = getCategory.await()

                    _stuffCategory.addAll(result.categoryData)
                    Timber.i(result.categoryData.toString())

                    setupCategoryName(result)

                } catch (e: Exception) {
                    Timber.e(e.message.toString1())
                }
            }
        }
    }

    /**
     * Get list of category from Manipulation model data
     */
    private fun setupCategoryName(responseData: RequestAllCategoryResponse){
        _categoryName.postValue(responseData.getCategoryName())
    }

    /**
     * Creating new stuff data.
     */
    fun saveNewStuffData(
        name: String,
        category: String,
        price: String,
        description: String) {

        viewModelScope.launch {

            withContext(Dispatchers.IO) {

                val createData = AuctionApi.retrofitService
                    .createNewStuffAsync(
                        getOfficerToken()!!,
                        setupCategoryId(category),
                        name,
                        price,
                        description,
                        currentTime()
                    )

                try {
                    val response = createData.await()
                    Timber.i(response.toString())
                    Timber.i(currentTime())
                    creationSuccess()
                } catch (e: Exception) {
                    Timber.e(e.message.toString1())
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

    fun onClickButton() {
        _clickState.value = true
    }

    /**
     * Set the value of click state to false again, to stop some event
     * or the event is finished.
     */
    fun restartClickState() {
        if (_clickState.value == true) {
            _clickState.value = false
        }
    }

    /**
     * To get triggered navigation to recent Fragment.
     */
    private fun creationSuccess() {
        _createSuccess.postValue(true)
    }

    fun restartCreationState() {
        if (_createSuccess.value == true) {
            _createSuccess.value == false
        }
    }

}
