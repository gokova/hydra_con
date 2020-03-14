package com.hydra.hydracon.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    private var database: FirebaseDatabase = Firebase.database
    var databaseReference: DatabaseReference = database.reference
}
