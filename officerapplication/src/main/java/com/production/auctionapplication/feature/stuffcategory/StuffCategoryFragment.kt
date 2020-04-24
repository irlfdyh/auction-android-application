package com.production.auctionapplication.feature.stuffcategory

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
import com.production.auctionapplication.adapter.CategoryListAdapter
import com.production.auctionapplication.adapter.StuffCategoryListener
import com.production.auctionapplication.databinding.FragmentStuffCategoryBinding
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.util.EventObserver

class StuffCategoryFragment : Fragment() {

    private lateinit var viewModel: StuffCategoryViewModel
    private lateinit var binding: FragmentStuffCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this)
                .get(StuffCategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stuff_category, container, false)

        // set binding lifecycle owner
        binding.lifecycleOwner = this
;
        binding.stuffCategoryList.adapter = CategoryListAdapter(StuffCategoryListener { data ->
            navigateToDetailCategoryData(data)
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Gave the binding access to the view model
        binding.viewModel = viewModel

        viewModel.clickState.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                navigateToCreateNewCategory()
            }
        })
    }

    private fun navigateToCreateNewCategory() {
        val action  =
            StuffCategoryFragmentDirections
                .actionStuffCategoryFragmentToCreateUpdateStuffCategoryFragment(
                    getString(R.string.create_data_text), null)
        findNavController().navigate(action)
    }

    private fun navigateToDetailCategoryData(args: CategoryResponse?) {
        val action =
            StuffCategoryFragmentDirections
                .actionStuffCategoryFragmentToCreateUpdateStuffCategoryFragment(
                    getString(R.string.update_data_text), args)
        findNavController().navigate(action)
    }

}
