package com.production.auctionapplication.feature.administrator.officer.createupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateOfficerBinding
import com.production.auctionapplication.util.setDropDownAdapter
import kotlinx.android.synthetic.main.fragment_create_update_officer.*

class CreateUpdateOfficerFragment : Fragment() {

    private lateinit var viewModel: CreateUpdateOfficerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate<FragmentCreateUpdateOfficerBinding>(
                inflater, R.layout.fragment_create_update_officer, container, false)

        val items = listOf("Active", "Deactive")
        val levelItems = listOf("Administrator", "Officer")

        val status =
            (binding.officerStatusDrop.editText as AutoCompleteTextView).apply {
                setAdapter(setDropDownAdapter(requireContext(), items))
            }

        val level =
            (binding.officerLevelDrop.editText as AutoCompleteTextView).apply {
                setAdapter(setDropDownAdapter(requireContext(), levelItems))
            }

        binding.createOfficerButton.setOnClickListener {
            saveData(status.text.toString(), level.text.toString())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(CreateUpdateOfficerViewModel::class.java)
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
            }
        }

    }

}
