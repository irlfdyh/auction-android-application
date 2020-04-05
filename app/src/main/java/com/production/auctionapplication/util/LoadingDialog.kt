package com.production.auctionapplication.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.production.auctionapplication.R

class LoadingDialog(private var activity: Activity, context: Context)
    : AlertDialog(context) {

    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder = Builder(activity)

        val inflater = activity.layoutInflater

        // set dialog layout
        builder.setView(inflater.inflate(
            R.layout.dialog_loading_layout, null)
        )
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}