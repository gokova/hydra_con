package com.hydra.hydracon.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    var database: FirebaseDatabase = Firebase.database
}
