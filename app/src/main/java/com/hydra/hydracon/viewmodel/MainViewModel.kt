package com.hydra.hydracon.viewmodel

import androidx.lifecycle.ViewModel
import com.hydra.hydracon.firebase.FirebaseRepository

class MainViewModel : ViewModel() {

    var firebaseRepository = FirebaseRepository()
}
