package com.production.auctionapplication.feature.stuff.createupdate

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateStuffBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.util.EventObserver
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import com.production.auctionapplication.util.setDropDownAdapter
import kotlinx.android.synthetic.main.fragment_create_update_stuff.*

class CreateUpdateStuffFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var viewModel: CreateUpdateStuffViewModel
    private lateinit var binding: FragmentCreateUpdateStuffBinding
    private val args: CreateUpdateStuffFragmentArgs by navArgs()

    private lateinit var category: AutoCompleteTextView
    private lateinit var dialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_create_update_stuff, container, false)

        application = requireActivity().application

        // setup loading dialog
        dialog = LoadingDialog(requireActivity(), application)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffViewModel::class.java)

        binding.viewModel = viewModel

        // Observing the category name, then set the dropdown item when the
        // value isn't null.
        viewModel.categoryName.observe(viewLifecycleOwner, EventObserver { categoryData ->
            category = (binding.stuffCategoryDrop.editText as AutoCompleteTextView).apply {
                setAdapter(setDropDownAdapter(requireContext(), categoryData))
            }
        })

        // observing click state
        viewModel.clickState.observe(viewLifecycleOwner, EventObserver { clicked ->
            if (clicked) {
                saveData(category.text.toString())
                hideSoftKeyboard(requireActivity())
            }
        })

        viewModel.showDialog.observe(viewLifecycleOwner, EventObserver { show ->
            if (show) {
                showDialog(true)
            } else if (!show) {
                showDialog(false)
            }
        })

        // Observing success create state
        viewModel.uploadSuccess.observe(viewLifecycleOwner, EventObserver { success ->
            if (success) {
                navigateToListFragment()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftKeyboard(requireActivity())
    }

    private fun saveData(category: String) {

        val name = stuff_name_input.editText
        val price = stuff_price_input.editText
        val description =  stuff_description_input.editText

        when {
            name?.text.isNullOrEmpty() -> {
                name?.error = getString(R.string.must_filled_field)
            }
            price?.text.isNullOrEmpty() -> {
                price?.error = getString(R.string.must_filled_field)
            }
            description?.text.isNullOrEmpty() -> {
                description?.error = getString(R.string.must_filled_field)
            }
            else -> {
                viewModel.onPrepareUploadData(
                    name?.text.toString(),
                    price?.text.toString(),
                    category,
                    description?.text.toString()
                )
            }
        }
    }

    private fun navigateToListFragment() {
        val action  =
            CreateUpdateStuffFragmentDirections
                .actionCreateUpdateStuffFragmentToStuffFragment()
        findNavController().navigate(action)
    }

    private fun showDialog(state: Boolean) {
        if (state) {
            dialog.showLoadingDialog()
        } else {
            dialog.hideLoadingDialog()
        }
    }

}