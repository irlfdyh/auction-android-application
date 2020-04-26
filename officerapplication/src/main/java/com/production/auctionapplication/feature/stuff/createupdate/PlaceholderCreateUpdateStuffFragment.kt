package com.production.auctionapplication.feature.stuff.createupdate

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentPlaceholderCreateUpdateStuffBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.repository.StuffDataTransfer
import com.production.auctionapplication.util.EventObserver

class PlaceholderCreateUpdateStuffFragment : Fragment() {

    private lateinit var binding: FragmentPlaceholderCreateUpdateStuffBinding
    private lateinit var viewModel: CreateUpdateStuffViewModel
    private lateinit var application: Application
    private val args: PlaceholderCreateUpdateStuffFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_placeholder_create_update_stuff, container, false
        )

        application = requireActivity().application

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.onStart(args.stuff)

        viewModel.startNavigate.observe(viewLifecycleOwner, EventObserver { ready ->
            if (ready) {

            }
        })
    }

    private fun navigateToCreateOfUpdateFragment(args: StuffDataTransfer) {

    }
}
