package com.production.auctionapplication.feature.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.production.auctionapplication.R
import com.production.auctionapplication.databinding.ActivitySigninBinding
import com.production.auctionapplication.feature.ViewModelFactory
import com.production.auctionapplication.feature.administrator.AdministratorActivity
import com.production.auctionapplication.feature.officer.OfficerMainActivity
import com.production.auctionapplication.repository.networking.models.auth.OfficerAuth
import com.production.auctionapplication.util.LoadingDialog
import com.production.auctionapplication.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_signin.*


class SigninActivity : AppCompatActivity() {

    private lateinit var viewModel: SigninViewModel
    private lateinit var dialog: LoadingDialog
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivitySigninBinding>(
                this, R.layout.activity_signin)

        binding.lifecycleOwner = this

        val application = requireNotNull(this).application

        // Creating instance of the viewModelFactory.
        val viewModelFactory =
            ViewModelFactory(application)

        // initialize the view model
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SigninViewModel::class.java)

        // setup loading dialog
        dialog = LoadingDialog(this, application)

        binding.viewModel = viewModel

        button = binding.loginButton

        viewModel.clickState.observe(this, Observer {
            if (it == true) {
                loginAction()
                hideSoftKeyboard(this)
            }
        })
    }

    /**
     * Function for handle user button Click
     */
    private fun loginAction() {

        // Getting reference to the EditText.
        val username = username_input.editText
        val password = password_input.editText

        when {
            username?.text.isNullOrEmpty() -> {
                username?.error = getString(R.string.empty_username)
            }
            password?.text.isNullOrEmpty() -> {
                password?.error = getString(R.string.empty_password)
            }
            else -> {
                viewModel.onSigninAction(
                    username?.text.toString(),
                    password?.text.toString()
                )
                loginCheck()
                showDialog(true)
                viewModel.restartClickState()
            }
        }
    }

    /**
     * Function for navigating to the right Activity, and checking are the login is
     * successfully.
     */
    private fun loginCheck() {
        viewModel.loginState.observe(this, Observer {
            // when the level value is 1 that means the user level is Administrator
            // otherwise is officer.
            when (it.levelId) {
                1 -> {
                    val intent = Intent(this, AdministratorActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this, OfficerMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    showDialog(false)
                    showSnackbar(it)
                }
            }
        })
    }

    private fun showSnackbar(data: OfficerAuth) {
        viewModel.showSnackbarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    data.message,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            // after showing the snackbar then change the value to false again.
            viewModel.doneShowingSnackbar()
        })
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

}