package com.production.auctionapplication.feature.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SigninViewModelTest {

    private lateinit var signinViewModel: SigninViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        signinViewModel = SigninViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun signInBehaviour_whenSignInFailed() {
        // WHEN - the signin is failed
        signinViewModel.checkLoginStatus()

        // THEN - make sure that the value is true
        assertThat(signinViewModel.showSnackbarEvent.value, `is`(true))
    }

    @Test
    fun signInBehaviour_whenSignInFailed_restartingTheClickState() {
        // GIVEN - simulate when the button is clicked
        signinViewModel.onButtonClick()

        // WHEN - the signin is failed
        signinViewModel.restartClickState()

        // THEN - make sure that the value is false
        assertThat(signinViewModel.clickState.value, `is`(false))
    }

    @Test
    fun signIn_Behaviour_whenSignInButton_isClicked() {
        // WHEN - the method is called from layout
        signinViewModel.onButtonClick()

        // THEN - make sure that the property value is true
        assertThat(signinViewModel.clickState.value, `is`(true))
    }

}