package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateStuffCategoryBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.util.EventObserver
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_create_update_stuff_category.*

class CreateUpdateStuffCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCreateUpdateStuffCategoryBinding
    private lateinit var viewModel: CreateUpdateStuffCategoryViewModel
    private val args: CreateUpdateStuffCategoryFragmentArgs by navArgs()

    private lateinit var dialog: LoadingDialog

    private lateinit var application: Application


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_update_stuff_category, container, false)

        binding.lifecycleOwner = this

        application = requireActivity().application

        // Setup the loading dialog
        dialog = LoadingDialog(requireActivity(), application)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffCategoryViewModel::class.java)

        // Bind View Model to the layout.
        binding.viewModel = viewModel

        // when the View Model is initialized call onStart function
        // to check if the [args.categoryData] is null that means
        // create new Category else is update data.
        viewModel.onStart(args.category)

        // the action will be started when the [clickState] is true
        viewModel.clickState.observe(viewLifecycleOwner, EventObserver { clicked ->
            if (clicked) {
                setData()
                hideSoftKeyboard(requireActivity())
            }
        })

        // Trigger navigation when the uploading data is success
        viewModel.uploadIsSuccess.observe(viewLifecycleOwner, EventObserver { success ->
            if (success) {
                navigateToListFragment()
            }
        })

        viewModel.showDialog.observe(viewLifecycleOwner, EventObserver { show ->
            if (show) {
                showDialog(true)
            } else if (!show) {
                showDialog(false)
                Toast.makeText(requireContext(), viewModel.uploadMessage.value.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftKeyboard(requireActivity())
    }

    /**
     * Handle button click action
     */
    private fun setData() {

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
                viewModel.onPrepareUploadData(
                    name?.text.toString(),
                    description?.text.toString()
                )
            }
        }
    }

    private fun navigateToListFragment() {
        val action
                = CreateUpdateStuffCategoryFragmentDirections
            .actionCreateUpdateStuffCategoryFragmentToStuffCategoryFragment("")
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
