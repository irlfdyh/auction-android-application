package com.production.auctionapplication.feature.administrator.officer.createupdate

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateOfficerBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.util.EventObserver
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_create_update_officer.*

class CreateUpdateOfficerFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var binding: FragmentCreateUpdateOfficerBinding
    private lateinit var viewModel: CreateUpdateOfficerViewModel
    private lateinit var dialog: LoadingDialog

    private val args: CreateUpdateOfficerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_create_update_officer, container, false)

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
            .get(CreateUpdateOfficerViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.onStart(args.officer)

        viewModel.clickState.observe(viewLifecycleOwner, EventObserver { clicked ->
            if (clicked) {
                onSaveData()
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

        viewModel.uploadSuccess.observe(viewLifecycleOwner, EventObserver { success ->
            if (success) {
                navigateToListFragment()
            }
        })

//        status = (binding.officerStatusDrop.editText as AutoCompleteTextView).apply {
//                setAdapter(setDropDownAdapter(requireContext(), viewModel.officerStatus))
//        }
//
//        level = (binding.officerLevelDrop.editText as AutoCompleteTextView).apply {
//                setAdapter(setDropDownAdapter(requireContext(), viewModel.officerLevel))
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftKeyboard(requireActivity())
    }

    /**
     * Handle get text from the View.
     */
    private fun onSaveData() {

        val level = officer_level_drop.editText
        val username = officer_username_input.editText
        val password = officer_password_input.editText
        val name = officer_name_input.editText
        val phone = officer_phone_input.editText
        val status = officer_status_drop.editText

        when {
            username?.text.isNullOrEmpty() -> {
                username?.error = getString(R.string.must_filled_field)
            }
            password?.text.isNullOrEmpty() -> {
                password?.error = getString(R.string.must_filled_field)
            }
            name?.text.isNullOrEmpty() -> {
                name?.error = getString(R.string.must_filled_field)
            }
            phone?.text.isNullOrEmpty() -> {
                phone?.error = getString(R.string.must_filled_field)
            }
            else -> {
                viewModel.onPrepareUploadData(
                    level?.text.toString(),
                    username?.text.toString(),
                    password?.text.toString(),
                    name?.text.toString(),
                    phone?.text.toString(),
                    status?.text.toString()
                )
            }
        }
    }

    private fun showDialog(state: Boolean) {
        if (state) {
            dialog.showLoadingDialog()
        } else {
            dialog.hideLoadingDialog()
        }
    }

    private fun navigateToListFragment() {
        val action  =
            CreateUpdateOfficerFragmentDirections
                .actionCreateUpdateOfficerFragmentToOfficerFragment()
        findNavController().navigate(action)
    }

}
