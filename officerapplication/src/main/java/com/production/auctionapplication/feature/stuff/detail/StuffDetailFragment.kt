package com.production.auctionapplication.feature.stuff.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentStuffDetailBinding

class StuffDetailFragment : Fragment() {

    private lateinit var viewModel: StuffDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(StuffDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentStuffDetailBinding>(
                inflater, R.layout.fragment_stuff_detail, container, false)

        return binding.root
    }

}
