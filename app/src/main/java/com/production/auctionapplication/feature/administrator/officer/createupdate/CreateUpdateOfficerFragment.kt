package com.production.auctionapplication.feature.administrator.officer.createupdate

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.production.auctionapplication.R

class CreateUpdateOfficerFragment : Fragment() {

    companion object {
        fun newInstance() = CreateUpdateOfficerFragment()
    }

    private lateinit var viewModel: CreateUpdateOfficerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_update_officer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateUpdateOfficerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
