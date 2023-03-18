package com.vintz.vintzplastics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vintz.vintzplastics.databinding.ActivityAuthGateBinding

class AuthGateActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "AuthGateActivity"
    }

    private lateinit var binding: ActivityAuthGateBinding
    private val logIn: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onLoginResult)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthGateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()

        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.AppleBuilder().build(),
            //AuthUI.IdpConfig.GitHubBuilder().build(),
            //AuthUI.IdpConfig.PhoneBuilder().build()
        )

        if (Firebase.auth.currentUser == null) {
            val loginIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_launcher_foreground)
                .setTheme(R.style.Theme_VintzPlastics)
                .setAvailableProviders(
                    providers
                )
                .build()

            logIn.launch(loginIntent)
        } else goToMainActivity()
    }

    private fun onLoginResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            goToMainActivity()
            Log.d(TAG, "Sign in successful")
        } else {
            Toast.makeText(this, "There was an error signing in", Toast.LENGTH_LONG)
                .show()

            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in cancelled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}