package com.hydra.hydracon.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hydra.hydracon.R
import com.hydra.hydracon.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    companion object {
        const val RC_SIGN_IN = 1
        const val RC_MAIN_ACTIVITY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        lifecycle.addObserver(viewModel)

        viewModel.signedInUser.observe(this, Observer { loggedInUser ->
            onAuthStateChanged(loggedInUser)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Timber.d("Signed in!")
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Timber.d("Sign in canceled!")
                finish()
            }
        } else if (requestCode == RC_MAIN_ACTIVITY && resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }

    private fun onAuthStateChanged(signedInUser: FirebaseUser?) {
        if (signedInUser != null) {
            startActivityForResult(Intent(this, MainActivity::class.java), RC_MAIN_ACTIVITY)
            viewModel.databaseReference.child("users").orderByChild("email")
                .equalTo(signedInUser.email).addValueEventListener(getEventListener())
        } else {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build())).build(),
                RC_SIGN_IN
            )
        }
    }

    private fun getEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = (dataSnapshot.value as ArrayList<*>?)?.firstOrNull()
                Timber.d("Get Data: $user")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.w("Load post onCanceled: ${databaseError.message}")
                Snackbar.make(
                    container,
                    "Load post onCanceled: ${databaseError.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
