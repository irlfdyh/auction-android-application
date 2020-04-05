package com.production.auctionapplication.feature.stuff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.adapter.StuffListAdapter
import com.production.auctionapplication.databinding.FragmentStuffBinding

class StuffFragment : Fragment() {

    private val viewModel: StuffViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StuffViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentStuffBinding>(
                inflater, R.layout.fragment_stuff, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.stuffList.adapter = StuffListAdapter()

        viewModel.clickHandler.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navigateToCreateNewStuff()
            }
        })

        return binding.root
    }

    private fun navigateToCreateNewStuff() {
        val action = StuffFragmentDirections
            .actionStuffFragmentToCreateUpdateStuffFragment()
        findNavController().navigate(action)
        viewModel.restartClickState()
    }

}
