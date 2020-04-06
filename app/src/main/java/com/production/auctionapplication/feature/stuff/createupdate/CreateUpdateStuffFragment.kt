package com.production.auctionapplication.feature.stuff.createupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateStuffBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import com.production.auctionapplication.util.setDropDownAdapter
import kotlinx.android.synthetic.main.fragment_create_update_stuff.*

class CreateUpdateStuffFragment : Fragment() {

    private lateinit var viewModel: CreateUpdateStuffViewModel
    private lateinit var category: AutoCompleteTextView
    private lateinit var dialog: LoadingDialog
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireActivity().application

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffViewModel::class.java)

        viewModel.getStuffCategory()

        // setup loading dialog
        dialog = LoadingDialog(requireActivity(), application)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentCreateUpdateStuffBinding>(
                inflater, R.layout.fragment_create_update_stuff, container, false)

        binding.viewModel = viewModel

        // Observing the category name, then set the dropdown item when the
        // value isn't null.
        viewModel.categoryName.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                category = (binding.stuffCategoryDrop.editText as AutoCompleteTextView).apply {
                    setAdapter(setDropDownAdapter(requireContext(), it))
                }
            }
        })

        // observing click state
        viewModel.clickState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                saveData(category.text.toString())
                hideSoftKeyboard(requireActivity())
            }
        })

        // Observing success create state
        viewModel.createSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showDialog(false)
                navigateToListFragment()
            }
        })

        button = binding.createStuffButton

        return binding.root
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
                viewModel.saveNewStuffData(
                    name?.text.toString(),
                    category,
                    price?.text.toString(),
                    description?.text.toString()
                )
                viewModel.restartClickState()
                showDialog(true)
            }
        }

    }

    private fun showDialog(state: Boolean) {
        // Disabled the button and showing the loading dialog
        if (state) {
            button.isEnabled = false
            dialog.showLoadingDialog()
        } else {
            dialog.hideLoadingDialog()
            button.isEnabled = true
        }
    }

    private fun navigateToListFragment() {
        val action  =
            CreateUpdateStuffFragmentDirections
                .actionCreateUpdateStuffFragmentToStuffFragment()
        findNavController().navigate(action)
        viewModel.restartCreationState()
    }

}
