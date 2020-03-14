package com.hydra.hydracon.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.hydra.hydracon.firebase.FirebaseRepository

class MainViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()
    val databaseReference: DatabaseReference get() = firebaseRepository.databaseReference
}
