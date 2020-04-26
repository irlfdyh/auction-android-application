package com.production.auctionapplication.feature.administrator.officer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.adapter.OfficerListAdapter
import com.production.auctionapplication.adapter.OfficerListener
import com.production.auctionapplication.databinding.FragmentOfficerBinding
import com.production.auctionapplication.repository.networking.models.officer.OfficerResponse
import com.production.auctionapplication.util.EventObserver

class OfficerFragment : Fragment() {

    private lateinit var viewModel: OfficerViewModel
    private lateinit var binding: FragmentOfficerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_officer, container, false)

        binding.lifecycleOwner = this

        binding.officerList.adapter = OfficerListAdapter(OfficerListener { data ->
            navigateToUpdateOfficerData(data)
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this)
            .get(OfficerViewModel::class.java)

        binding.viewModel = viewModel

        // trigger navigation to the create new fragment
        viewModel.clickState.observe(viewLifecycleOwner, EventObserver{ clicked ->
            if (clicked) {
                navigateToCreateNewOfficer()
            }
        })
    }

    private fun navigateToCreateNewOfficer() {
        val action =
            OfficerFragmentDirections
                .actionOfficerFragmentToCreateUpdateOfficerFragment(
                    getString(R.string.create_data_text), null)
        findNavController().navigate(action)
    }

    private fun navigateToUpdateOfficerData(args: OfficerResponse?) {
        val action =
            OfficerFragmentDirections
                .actionOfficerFragmentToCreateUpdateOfficerFragment(
                    getString(R.string.update_data_text), args)
        findNavController().navigate(action)
    }

}
