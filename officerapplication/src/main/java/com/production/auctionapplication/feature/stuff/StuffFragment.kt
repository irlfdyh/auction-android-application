package com.production.auctionapplication.feature.stuff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.adapter.StuffListAdapter
import com.production.auctionapplication.adapter.StuffListener
import com.production.auctionapplication.databinding.FragmentStuffBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.repository.networking.models.stuff.StuffResponse
import com.production.auctionapplication.util.EventObserver

class StuffFragment : Fragment() {

    private lateinit var viewModel: StuffViewModel
    private lateinit var binding: FragmentStuffBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stuff, container, false)

        binding.lifecycleOwner = this

        binding.stuffList.adapter = StuffListAdapter(StuffListener { data ->
            navigateToUpdateStuffData(data)
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireActivity().application

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(StuffViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.clickHandler.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                navigateToCreateNewStuff()
            }
        })
    }

    private fun navigateToCreateNewStuff() {
        val action = StuffFragmentDirections
            .actionStuffFragmentToCreateUpdateStuffFragment(
                getString(R.string.create_data_text), null
            )
        findNavController().navigate(action)
    }

    private fun navigateToUpdateStuffData(args: StuffResponse?) {
        val action = StuffFragmentDirections
            .actionStuffFragmentToCreateUpdateStuffFragment(
                getString(R.string.update_data_text), args
            )
        findNavController().navigate(action)
    }

}
