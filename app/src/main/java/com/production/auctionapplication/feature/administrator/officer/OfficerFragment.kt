package com.production.auctionapplication.feature.administrator.officer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.adapter.OfficerListAdapter
import com.production.auctionapplication.databinding.FragmentOfficerBinding

class OfficerFragment : Fragment() {

    private val viewModel: OfficerViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(OfficerViewModel::class.java)
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

        // trigger navigation to navigate to the create fragment.
        binding.toCreateFragmentFab.setOnClickListener { navigateToCreateNewOfficer() }

        return binding.root
    }

    private fun navigateToCreateNewOfficer() {
        val action =
            OfficerFragmentDirections.actionOfficerFragmentToCreateUpdateOfficerFragment()
        findNavController().navigate(action)
    }

}
