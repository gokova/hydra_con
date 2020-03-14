package com.hydra.hydracon.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class SplashViewModel : ViewModel(), LifecycleObserver {

    private val _signedInUser = MutableLiveData<FirebaseUser?>()
    val signedInUser: LiveData<FirebaseUser?> get() = _signedInUser

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        Timber.d("AuthStateListener = currentUser.email: ${it.currentUser?.email}")
        _signedInUser.postValue(it.currentUser)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addAuthStateListener() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeAuthStateListener() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}
