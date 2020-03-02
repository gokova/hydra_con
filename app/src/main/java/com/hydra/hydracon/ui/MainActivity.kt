package com.hydra.hydracon.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hydra.hydracon.R
import com.hydra.hydracon.firebase.model.Guest
import com.hydra.hydracon.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var fireBaseDatabase: DatabaseReference

    companion object {
        const val MESSAGES_CHILD = "messages"
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Snackbar.make(container, R.string.title_home, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    Snackbar.make(container, R.string.title_dashboard, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        fireBaseDatabase = Firebase.database.reference
        fireBaseDatabase.child(MESSAGES_CHILD).addValueEventListener(getEventListener())

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun getEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
//                val post = dataSnapshot.getValue(Post::class.java)
//                post?.let {
//                    postAuthor.text = it.author
//                    postTitle.text = it.title
//                    postBody.text = it.body
//                }
                for (postSnapshot in dataSnapshot.children) {
                    val guest: Guest? = postSnapshot.getValue(Guest::class.java)
                    Timber.d("Get Data: ${guest?.toString() ?: ""}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Timber.w("loadPost:onCancelled", databaseError.toException())
                Toast.makeText(
                    baseContext, "Failed to load post.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
