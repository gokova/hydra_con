package com.hydra.hydracon.viewmodel

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.hydra.hydracon.firebase.FirebaseRepository
import timber.log.Timber

class SplashViewModel : ViewModel(), LifecycleObserver {

    private val _signedInUser = MutableLiveData<FirebaseUser?>()
    val signedInUser: LiveData<FirebaseUser?> get() = _signedInUser

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseRepository = FirebaseRepository()
    val databaseReference: DatabaseReference get() = firebaseRepository.databaseReference

    var isAuthListenerCalled = false

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        if (!isAuthListenerCalled) {
            Timber.d("AuthStateListener = currentUser.email: ${it.currentUser?.email}")
            _signedInUser.postValue(it.currentUser)
            isAuthListenerCalled = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addAuthStateListener() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
        isAuthListenerCalled = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeAuthStateListener() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}
