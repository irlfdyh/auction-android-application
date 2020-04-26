package com.production.auctionapplication.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Build.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.production.auctionapplication.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Currently used for hiding the system soft keyboard
 */
fun hideSoftKeyboard(activity: Activity) {
    val imm =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus

    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }

    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * This adapter is used for every dropdown text input.
 */
fun setDropDownAdapter(context: Context, items: List<*>): ArrayAdapter<*> {
    return ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, items)
}

@SuppressLint("SimpleDateFormat")
fun currentTime(): String {
    return if (VERSION.SDK_INT >= VERSION_CODES.O) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        current.format(formatter).toString()
    } else {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        formatter.format(date).toString()
    }
}

/**
 * Code for callback message
 */
const val ERROR_CTO = 1
const val ERROR_NO_RESPONSE = 2
const val REQUEST_CREATE_DATA_SUCCESS = 3
const val REQUEST_CREATE_DATA_FAILED = 4
const val BUTTON_CREATE_CODE = 111
const val BUTTON_UPDATE_CODE = 222