package com.example.myandroidapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Setup Navigation
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        navController = navHostFragment.navController
        navController?.let {
            NavigationUI.setupActionBarWithNavController(this, it)
        }

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.mainActivityBottomNavigationView)
        navController?.let { NavigationUI.setupWithNavController(bottomNavigationView, it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        updateMenuVisibility(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp() ?: super.onBackPressed()
                true
            }

            R.id.action_sign_out -> {
                signOut()
                true
            }

            R.id.action_sign_in -> {
                navController?.navigate(R.id.action_studentsFragment_to_loginFragment)
                true
            }

            R.id.addUserFragment -> {
                navController?.navigate(R.id.action_global_addStudentFragment)
                true
            }

            R.id.action_all_posts -> {
                navController?.navigate(R.id.action_studentsFragment_to_allPostsFragment)
                true
            }

            R.id.action_user_posts -> {
                navController?.navigate(R.id.action_studentsFragment_to_userPostsFragment)
                true
            }

            R.id.action_edit_profile -> {
                val currentUser = auth.currentUser
                val userName = currentUser?.displayName ?: "Default User"
                val userImageUri = currentUser?.photoUrl?.toString() ?: ""

                val bundle = Bundle().apply {
                    putString("userName", userName)
                    putString("userImageUri", userImageUri)
                }

                navController?.navigate(R.id.action_studentsFragment_to_editProfileFragment, bundle)
                true
            }

            else -> navController?.let {
                NavigationUI.onNavDestinationSelected(item, it)
            } ?: super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()  // Refresh the menu items
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        updateUI(null)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in
            // Perform any updates if necessary
        } else {
            // No user is signed in
            // Optionally perform any updates
        }
        invalidateOptionsMenu()  // Refresh the menu items to show/hide based on authentication status
    }

    private fun updateMenuVisibility(menu: Menu?) {
        val signInMenuItem = menu?.findItem(R.id.action_sign_in)
        val signOutMenuItem = menu?.findItem(R.id.action_sign_out)
        val allPostsMenuItem = menu?.findItem(R.id.action_all_posts)
        val userPostsMenuItem = menu?.findItem(R.id.action_user_posts)
        val editProfileMenuItem = menu?.findItem(R.id.action_edit_profile)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            signInMenuItem?.isVisible = false
            signOutMenuItem?.isVisible = true
            allPostsMenuItem?.isVisible = true
            userPostsMenuItem?.isVisible = true
            editProfileMenuItem?.isVisible = true
        } else {
            signInMenuItem?.isVisible = true
            signOutMenuItem?.isVisible = false
            allPostsMenuItem?.isVisible = false
            userPostsMenuItem?.isVisible = false
            editProfileMenuItem?.isVisible = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}

