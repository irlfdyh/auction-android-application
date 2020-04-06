package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateStuffCategoryBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_create_update_stuff_category.*

class CreateUpdateStuffCategoryFragment : Fragment() {

    private lateinit var viewModel: CreateUpdateStuffCategoryViewModel
    private lateinit var dialog: LoadingDialog
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireActivity().application

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffCategoryViewModel::class.java)

        dialog = LoadingDialog(requireActivity(), application)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentCreateUpdateStuffCategoryBinding>(
                inflater, R.layout.fragment_create_update_stuff_category, container, false)

        binding.viewModel = viewModel
        button = binding.createButton

        viewModel.clickState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                saveData()
                hideSoftKeyboard(requireActivity())
            }
        })

        viewModel.createSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showDialog(false)
                navigateToListFragment()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftKeyboard(requireActivity())
    }

    /**
     * Handle button click action
     */
    private fun saveData() {

        // Get reference to the EditText
        val name = category_name.editText
        val description = category_description.editText

        when {
            name?.text.isNullOrEmpty() -> {
                name?.error = getString(R.string.must_filled_field)
            }
            description?.text.isNullOrEmpty() -> {
                description?.error = getString(R.string.must_filled_field)
            }
            else -> {
                viewModel.onSaveNewData(
                    name?.text.toString(),
                    description?.text.toString()
                )
                viewModel.restartClickState()
                showDialog(true)
            }
        }

    }

    private fun showDialog(state: Boolean) {
        if (state) {
            button.isEnabled = false
            dialog.showLoadingDialog()
        } else {
            dialog.hideLoadingDialog()
            button.isEnabled = true
        }
    }

    private fun navigateToListFragment() {
        val action = CreateUpdateStuffCategoryFragmentDirections
            .actionCreateUpdateStuffCategoryFragmentToStuffCategoryFragment()
        findNavController().navigate(action)
        viewModel.restartCreationState()
    }

}
