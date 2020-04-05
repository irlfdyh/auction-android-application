package com.production.auctionapplication.feature.stuffcategory.createupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.FragmentCreateUpdateStuffCategoryBinding
import com.production.auctionapplication.feature.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_update_stuff_category.*

class CreateUpdateStuffCategoryFragment : Fragment() {

    private lateinit var viewModel: CreateUpdateStuffCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentCreateUpdateStuffCategoryBinding>(
                inflater, R.layout.fragment_create_update_stuff_category, container, false)

        val application = requireActivity().application

        val viewModelFactory =
            ViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CreateUpdateStuffCategoryViewModel::class.java)

        binding.viewModel = viewModel

        binding.createButton.setOnClickListener{ saveData() }

        return binding.root
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
                create_button.isEnabled = false
            }
        }

    }

}
