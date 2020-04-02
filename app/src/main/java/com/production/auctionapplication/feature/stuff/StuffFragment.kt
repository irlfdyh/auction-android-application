package com.production.auctionapplication.feature.stuff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

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
            FragmentStuffBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.stuffList.adapter = StuffListAdapter()

        return binding.root
    }

}
