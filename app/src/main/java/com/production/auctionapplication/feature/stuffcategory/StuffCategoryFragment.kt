package com.production.auctionapplication.feature.stuffcategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.production.auctionapplication.adapter.CategoryListAdapter
import com.production.auctionapplication.adapter.OnClickListener
import com.production.auctionapplication.databinding.FragmentStuffCategoryBinding

class StuffCategoryFragment : Fragment() {

    /**
     * Initialize the view model
     */
    private val viewModel: StuffCategoryViewModel by lazy {
        ViewModelProviders.of(this).get(StuffCategoryViewModel::class.java)
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
        binding.stuffCategoryList.adapter = CategoryListAdapter(OnClickListener {
            Toast.makeText(requireContext(), "Item Clicked", Toast.LENGTH_SHORT).show()
        })

        binding.navigateFab.setOnClickListener { navigateToCreateNewCategory() }

        return binding.root
    }

    private fun navigateToCreateNewCategory() {
        val action  =
            StuffCategoryFragmentDirections
                .actionStuffCategoryFragmentToCreateUpdateStuffCategoryFragment(null,"title")
        findNavController().navigate(action)
    }

}
