package com.production.auctionapplication.feature.stuff.createupdate

import android.app.Application
import androidx.lifecycle.*
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.OfficerRepository
import com.production.auctionapplication.repository.StuffDataTransfer
import com.production.auctionapplication.repository.database.OfficerAccountData
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.networking.AuctionApi
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.repository.networking.models.category.RequestAllCategoryResponse
import com.production.auctionapplication.repository.networking.models.category.getCategoryName
import com.production.auctionapplication.repository.networking.models.stuff.StuffResponse
import com.production.auctionapplication.util.*
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
    private val categoryNameKeyValue
            = mutableMapOf<String, CategoryResponse>()

    private val categoryIdKeyValue
            = mutableMapOf<Int?, CategoryResponse>()

    /**
     * to get the [categoryId] from the input key which it is the same item
     */
     private fun setupCategoryId(key: String): Int {
        val category =
            _stuffCategory.associateByTo(categoryNameKeyValue) {
                it.categoryName
            }
        return category[key]?.categoryId!!
    }

    /**
     * Get [categoryName] from API response that just return [categoryId]
     */
    private fun setupCategoryName(key: Int): String {
        val category =
            _stuffCategory.associateByTo(categoryIdKeyValue) {
                it.categoryId
            }
        return category[key]?.categoryName ?: "Gagal"
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
    private var _categoryName = MutableLiveData<Event<List<String?>>>()
    val categoryName: LiveData<Event<List<String?>>>
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

    private var _stuffUpdateData = MutableLiveData<StuffDataTransfer>()
    val stuffUpdateData: LiveData<StuffDataTransfer>
        get() = _stuffUpdateData

    /**
     * when this value is true, that means the data is ready, then navigate
     * to update the data.
     */
    private var _startNavigate = MutableLiveData<Event<Boolean>>()
    val startNavigate: LiveData<Event<Boolean>>
        get() = _startNavigate

    /**
     * Get any message from request or error message from exception
     */
    private var _uploadStateCode = MutableLiveData<Event<Int>>()
    private var _responseMessage = MutableLiveData<String>()
    val uploadMessage: LiveData<String> = Transformations.map(_uploadStateCode) { code ->
        when (code) {
            Event(REQUEST_CREATE_DATA_FAILED) -> application.getString(R.string.requset_failed_message)
            Event(REQUEST_CREATE_DATA_SUCCESS) -> _responseMessage.value.toString()
            else -> null
        }
    }

    /**
     *  Setup default value for new or old data is false
     */
    private var _isNewStuff: Boolean = false
    private var _stuffId =  MutableLiveData<String>()
    private var _buttonTextCode = MutableLiveData<Int>()

    var buttonText = Transformations.map(_buttonTextCode) {
        when (it) {
            BUTTON_CREATE_CODE -> application.getString(R.string.create_text)
            else -> application.getString(R.string.update_text)
        }
    }

    /**
     * Get list of category from Manipulation model data
     */
    private fun setupCategoryName(responseData: RequestAllCategoryResponse){
        _categoryName.postValue(Event(responseData.getCategoryName()))
    }

    /**
     * Called at placeholder fragment
     */
    fun onStart(stuffData: StuffResponse?) {
        viewModelScope.launch {

            // initialize button enable to true
            _buttonEnable.value = true
            // initialize stuffId value is present
            _stuffId.value = stuffData?.stuffId.toString()

            withContext(Dispatchers.IO) {

                // get all stuff category to setUp spinner array
                val getCategory =
                    AuctionApi.retrofitService.getAllCategoryAsync()

                try {
                    val result = getCategory.await()
                    Timber.i(result.categoryData.toString())

                    if (result.categoryData.isNotEmpty()) {

                        _stuffCategory.addAll(result.categoryData)
                        setupCategoryName(result)
                    } else {
                        Timber.i("onStart : _stuffCategory.isEmpty called")
                    }
                } catch (e: Exception) {
                    Timber.i("onStart : ${e.message.toString()}")

                    _showDialog.postValue(Event(false))
                }
            }
        }
    }

    /**
     *
     */
    fun onSetupData(stuffData: StuffDataTransfer?) {
        if (stuffData == null) {
            _isNewStuff = true
            _buttonTextCode.value = BUTTON_CREATE_CODE
        } else {
            stuffName.value = stuffData.stuffName
            stuffPrice.value = stuffData.startedPrice.toString()
            stuffDescription.value = stuffData.description
            stuffCategory.value = stuffData.stuffCategory

            _buttonTextCode.value = BUTTON_UPDATE_CODE

            Timber.i("onStart : ${_startNavigate.value.toString()}")
        }

    }

    fun onPrepareUploadData(stuffName: String, stuffPrice: String, category: String, stuffDesc: String) {

        // initial this property value
        _uploadSuccess.value = Event(false)
        _showDialog.value = Event(true)
        _buttonEnable.value = false

        if (_isNewStuff || _stuffId.value.isNullOrEmpty()) {
            onCreateData(stuffName, category, stuffPrice, stuffDesc)
        } else {
            onUpdateData(_stuffId.value!!, stuffName, category, stuffPrice, stuffDesc)
        }
    }

    /**
     * Creating new stuff data.
     */
    private fun onCreateData(name: String, category: String, price: String, desc: String) {
        viewModelScope.launch {

            Timber.i("Request Started : name=$name, category=$category, price=$price, desc=$desc")
            Timber.i(category)

            withContext(Dispatchers.IO) {

                val getResponse =
                    AuctionApi.retrofitService
                        .createNewStuffAsync(
                            getOfficerAccount()?.token, setupCategoryId(category),
                            getOfficerAccount()?.officerId, name, price,
                            desc, currentTime(), ""
                    )

                try {
                    val response = getResponse.await()
                    Timber.i(response.toString())
                    Timber.i(currentTime())

                    if (response.stuffData != null) {

                        _showDialog.postValue(Event(false))
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_SUCCESS))
                        _uploadSuccess.postValue(Event(true))
                    } else {

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadSuccess.postValue(Event(false))
                    }
                } catch (e: Exception) {
                    Timber.e(e.message.toString())

                    _showDialog.postValue(Event(false))
                    _buttonEnable.postValue(true)
                    _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                    _uploadSuccess.postValue(Event(false))
                }
            }
        }
    }

    private fun onUpdateData(stuffId: String, name: String, category: String, price: String, desc: String) {
        viewModelScope.launch {

            Timber.i(stuffId)

            withContext(Dispatchers.IO) {

                val getAction =
                    AuctionApi.retrofitService
                        .updateStuffAsync(
                            getOfficerAccount()?.token,
                            stuffId, setupCategoryId(category),
                            name, price, desc
                        )

                try {
                    val response = getAction.await()
                    Timber.i(response.toString())

                    if (response.stuffData != null) {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadSuccess.postValue(Event(false))
                    } else {
                        _responseMessage.postValue(response.message)

                        _showDialog.postValue(Event(false))
                        _buttonEnable.postValue(true)
                        _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                        _uploadSuccess.postValue(Event(false))
                    }

                } catch (e: Exception) {
                    Timber.e(e.message.toString())

                    _showDialog.postValue(Event(false))
                    _buttonEnable.postValue(true)
                    _uploadStateCode.postValue(Event(REQUEST_CREATE_DATA_FAILED))
                    _uploadSuccess.postValue(Event(false))
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