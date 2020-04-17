package com.production.auctionapplication.feature.administrator.officer.createupdate

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
import com.production.auctionapplication.databinding.FragmentCreateUpdateOfficerBinding
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import com.production.auctionapplication.util.setDropDownAdapter
import kotlinx.android.synthetic.main.fragment_create_update_officer.*

class CreateUpdateOfficerFragment : Fragment() {

    private lateinit var viewModel: CreateUpdateOfficerViewModel
    private lateinit var dialog: LoadingDialog
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireActivity().application

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CreateUpdateOfficerViewModel::class.java)

        // setup loading dialog
        dialog = LoadingDialog(requireActivity(), application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentCreateUpdateOfficerBinding>(
                inflater, R.layout.fragment_create_update_officer, container, false)

        binding.viewModel = viewModel
        button = binding.createOfficerButton

        val items = listOf("Active", "Deactive")
        val levelItems = listOf("Admin", "Petugas")

        val status =
            (binding.officerStatusDrop.editText as AutoCompleteTextView).apply {
                setAdapter(setDropDownAdapter(requireContext(), items))
            }

        val level =
            (binding.officerLevelDrop.editText as AutoCompleteTextView).apply {
                setAdapter(setDropDownAdapter(requireContext(), levelItems))
            }

        viewModel.clickState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                saveData(status.text.toString(), level.text.toString())
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
     * Handle get text from the View.
     */
    private fun saveData(status: String?, level: String?) {

        val username = officer_username_input.editText
        val password = officer_password_input.editText
        val name = officer_name_input.editText
        val phone = officer_phone_input.editText

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
                viewModel.onNewOfficerData(
                    level!!,
                    username?.text.toString(),
                    password?.text.toString(),
                    name?.text.toString(),
                    phone?.text.toString(),
                    status!!
                )
                showDialog(true)
                viewModel.restartClickState()
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
            CreateUpdateOfficerFragmentDirections
                .actionCreateUpdateOfficerFragmentToOfficerFragment()
        findNavController().navigate(action)
        viewModel.restartCreationState()
    }

}
