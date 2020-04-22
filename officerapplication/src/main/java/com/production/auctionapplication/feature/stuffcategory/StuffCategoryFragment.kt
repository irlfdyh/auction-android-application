package com.production.auctionapplication.feature.stuffcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.adapter.CategoryListAdapter
import com.production.auctionapplication.adapter.StuffCategoryListener
import com.production.auctionapplication.databinding.FragmentStuffCategoryBinding

class StuffCategoryFragment : Fragment() {

    private lateinit var viewModel: StuffCategoryViewModel

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
        val binding
                = FragmentStuffCategoryBinding.inflate(inflater)

        // set binding lifecycle owner
        binding.lifecycleOwner = this

        // Gave the binding access to the view model
        binding.viewModel = viewModel
;
        binding.stuffCategoryList.adapter = CategoryListAdapter(StuffCategoryListener { categoryId ->
            navigateToDetailCategoryData(categoryId.toString())
        })

        binding.navigateFab.setOnClickListener { navigateToCreateNewCategory() }

        return binding.root
    }

    private fun navigateToCreateNewCategory() {
        val action  =
            StuffCategoryFragmentDirections
                .actionStuffCategoryFragmentToCreateUpdateStuffCategoryFragment(
                    null,  getString(R.string.create_data_text))
        findNavController().navigate(action)
    }

    private fun navigateToDetailCategoryData(args: String?) {
        val action =
            StuffCategoryFragmentDirections
                .actionStuffCategoryFragmentToCreateUpdateStuffCategoryFragment(
                    args, getString(R.string.update_data_text))
        findNavController().navigate(action)
    }

}
