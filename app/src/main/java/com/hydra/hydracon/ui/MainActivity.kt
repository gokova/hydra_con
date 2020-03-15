package com.hydra.hydracon.ui

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hydra.hydracon.R
import com.hydra.hydracon.firebase.model.Guest
import com.hydra.hydracon.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    companion object {
        const val MESSAGES = "messages"
    }

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Snackbar.make(container, R.string.title_home, Snackbar.LENGTH_LONG).show()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    Snackbar.make(container, R.string.title_dashboard, Snackbar.LENGTH_LONG).show()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.databaseReference.child(MESSAGES).addValueEventListener(getEventListener())

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show()
        }

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                AuthUI.getInstance().signOut(this).addOnCompleteListener {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val guest: Guest? = postSnapshot.getValue(Guest::class.java)
                    Timber.d("Get Data: ${guest?.toString() ?: ""}")
                }
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
