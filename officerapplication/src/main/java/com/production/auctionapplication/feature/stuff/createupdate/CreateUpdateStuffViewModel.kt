package com.production.auctionapplication.feature.stuff.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.database.OfficerAccountData
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.repository.networking.models.category.RequestAllCategoryResponse
import com.production.auctionapplication.repository.networking.models.category.getCategoryName
import com.production.auctionapplication.repository.networking.models.stuff.StuffResponse
import com.production.auctionapplication.util.Event
import com.production.auctionapplication.util.REQUEST_CREATE_DATA_FAILED
import com.production.auctionapplication.util.REQUEST_CREATE_DATA_SUCCESS
import com.production.auctionapplication.util.currentTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateUpdateStuffViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * reference to get the user token from the local database.
     */
    private val repository =
        OfficerRepository(OfficerDatabase.getInstance(application))

    /**
     * properties is used to hold data from response
     */
    private var _stuffCategory
            = mutableListOf<CategoryResponse>()

    /**
     * properties to setup key and value to get id from item that selected
     * from the drop down.
     */
    private val setKeyValue
            = mutableMapOf<String?, CategoryResponse>()

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
     * property value to bind the layout
     */
    var stuffName = MutableLiveData<String>()
    var stuffCategory = MutableLiveData<String>()
    var stuffPrice = MutableLiveData<String>()
    var stuffDescription = MutableLiveData<String>()

    /**
     * Properties to hold the category name
     */
    private var _categoryName = MutableLiveData<List<String?>>()
    val categoryName: LiveData<List<String?>>
        get () = _categoryName

    /**
     * to handle the click button with triggered events.
     */
    private var _clickState = MutableLiveData<Event<Boolean>>()
    val clickState: LiveData<Event<Boolean>>
        get() = _clickState

    /**
     * to trigger navigation to the stuff list fragment.
     */
    private var _uploadSuccess = MutableLiveData<Event<Boolean>>()
    val uploadSuccess: LiveData<Event<Boolean>>
        get() = _uploadSuccess

    private var _buttonEnable = MutableLiveData<Boolean>()
    val buttonEnable: LiveData<Boolean>
        get() = _buttonEnable

    private var _showDialog = MutableLiveData<Event<Boolean>>()
    val showDialog: LiveData<Event<Boolean>>
        get() = _showDialog

    /**
     * Get any message from request or error message from exception
     */
    private var _uploadCode = MutableLiveData<Event<Int>>()

    private var _responseMessage = MutableLiveData<String>()

    val uploadMessage: LiveData<String> = Transformations.map(_uploadCode) { code ->
        when (code) {
            Event(REQUEST_CREATE_DATA_FAILED) -> application.getString(R.string.requset_failed_message)
            Event(REQUEST_CREATE_DATA_SUCCESS) -> _responseMessage.value.toString()
            else -> null
        }
    }

    /**
     *  Setup default value for new or old data is false
     */
    private var isNewStuff: Boolean = false

    fun onStart(stuffData: StuffResponse?) {
        getStuffCategory()
        if (stuffData == null) {
            isNewStuff = true
        } else {
            stuffName.value = stuffData.stuffName
            stuffPrice.value = stuffData.startedPrice.toString()
            stuffDescription.value = stuffData.description
        }

    }

    /**
     * Get stuff category from the API.
     */
    private fun getStuffCategory() {
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
                    Timber.e(e.message.toString())
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

    fun uploadState(stuffId: String?, stuffName: String, stuffPrice: String, category: String,stuffDesc: String) {

        // initial this property value
        _uploadSuccess.value = Event(false)

        if (isNewStuff || stuffId.isNullOrEmpty()) {

        }

    }

    /**
     * Creating new stuff data.
     */
    fun onCreateStuff(name: String, category: String, price: String, description: String) {

        viewModelScope.launch {

            withContext(Dispatchers.IO) {

                val createData = AuctionApi.retrofitService
                    .createNewStuffAsync(
                        getOfficerAccount()?.token,
                        setupCategoryId(category),
                        getOfficerAccount()?.officerId,
                        name,
                        price,
                        description,
                        currentTime(),
                        ""
                    )

                try {
                    val response = createData.await()
                    Timber.i(response.toString())
                    Timber.i(currentTime())
                } catch (e: Exception) {
                    Timber.e(e.message.toString())
                }
            }
        }


    }

    /**
     * the request parameter is need token to verify the user, so this function
     * is used to get the officer token from the database.
     */
    private suspend fun getOfficerAccount(): OfficerAccountData? {
        return withContext(Dispatchers.IO) {
            // get user data from repository
            val data = repository.getAccountData()
            // return user internal data
            data
        }
    }

    fun onClickAction() {
        _clickState.value = Event(true)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}
