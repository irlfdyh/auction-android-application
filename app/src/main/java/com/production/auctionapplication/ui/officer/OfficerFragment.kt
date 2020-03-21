package com.production.auctionapplication.ui.officer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.production.auctionapplication.adapter.OfficerListAdapter
import com.production.auctionapplication.databinding.FragmentOfficerBinding

class OfficerFragment : Fragment() {

    private val viewModel: OfficerViewModel by lazy {
        ViewModelProviders.of(this).get(OfficerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            FragmentOfficerBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.officerList.adapter = OfficerListAdapter()

        return binding.root
    }

}
