package com.production.auctionapplication.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.production.auctionapplication.R
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.database.OfficerAuth
import com.production.auctionapplication.ui.administrator.AdministratorActivity
import com.production.auctionapplication.ui.officer.OfficerMainActivity
import com.production.auctionapplication.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_signin.*


class SigninActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: SigninViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val application = requireNotNull(this).application

        // Creating instance of the viewModelFactory.
        val viewModelFactory = SigninViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SigninViewModel::class.java)

        login_button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button -> {
                loginAction()
                hideSoftKeyboard(this)
            }
        }
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
                // Disabled the button and showing the Progressbar
                login_button.isEnabled = false
                signin_progress.visibility = View.VISIBLE

                loginCheck()
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
                    showSnackbar(it)

                    login_button.isEnabled = true
                    signin_progress.visibility = View.GONE
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

//    private fun showLoadingDialog(): AlertDialog {
//        val builder: AlertDialog.Builder
//        val alertDialog: AlertDialog
//
//        val mContext: Context = applicationContext
//        val inflater: LayoutInflater =
//            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val layout: View =
//            inflater.inflate(R.layout.stuff_row_item, findViewById(R.id.layout_root))
//
//        builder = AlertDialog.Builder(mContext)
//        builder.setView(layout)
//        alertDialog = builder.create()
//
//        return alertDialog
//    }

}




/**
 * Dialog example with timer
 */

//AlertDialog dialog = new AlertDialog.Builder(this)
//.setTitle("Notification Title")
//.setMessage("Do you really want to delete the file?")
//.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        // TODO: Add positive button action code here
//    }
//})
//.setNegativeButton(android.R.string.no, null)
//.create();
//dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//    private static final int AUTO_DISMISS_MILLIS = 6000;
//    @Override
//    public void onShow(final DialogInterface dialog) {
//        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
//        final CharSequence negativeButtonText = defaultButton.getText();
//        new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            defaultButton.setText(String.format(
//                Locale.getDefault(), "%s (%d)",
//                negativeButtonText,
//                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
//            ));
//        }
//        @Override
//        public void onFinish() {
//            if (((AlertDialog) dialog).isShowing()) {
//                dialog.dismiss();
//            }
//        }
//    }.start();
//    }
//});
//dialog.show();