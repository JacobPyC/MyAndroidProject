package com.example.myandroidapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myandroidapplication.Model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: SignInButton
//    private lateinit var progressBar: ProgressBar

    private val RC_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        signInButton = view.findViewById(R.id.signInButton)
//        progressBar = view.findViewById(R.id.progressBar)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signInButton.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
//        progressBar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!)
            }
        } catch (e: ApiException) {
            Log.w("LoginFragment", "Google sign in failed", e)
            Toast.makeText(requireContext(), "Google sign-in failed.", Toast.LENGTH_SHORT).show()
//            progressBar.visibility = View.GONE
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
//                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign in success, navigate to the main fragment
                    Toast.makeText(requireContext(), "Google sign-in successful", Toast.LENGTH_SHORT).show()
//                    findNavController()?.navigate(R.id.action_studentsFragment_to_allPostsFragment)
                    requireActivity().invalidateOptionsMenu()
                    findNavController().navigateUp()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    Log.w("LoginFragment", "signInWithCredential:failure", task.exception)
                }
            }
    }
//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var auth: FirebaseAuth
//    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
//
//    companion object {
//        private const val RC_SIGN_IN = 9001
//        private const val TAG = "LoginFragment"
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_login, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
//
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.your_web_client_id)) // Replace with your Web client ID
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//
//        // Set click listener for Google Sign-In button
//        view.findViewById<SignInButton>(R.id.signInButton).setOnClickListener {
//            signIn()
//        }
//
//    }
//
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                Log.d(TAG, "firebaseAuthWithGoogle: " + account.id)
//                firebaseAuthWithGoogle(account)
//            } catch (e: ApiException) {
//                Log.w(TAG, "Google sign in failed", e)
//                updateUI(null)
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
//        account?.let {
//            val credential = GoogleAuthProvider.getCredential(it.idToken, null)
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener(requireActivity()) { task ->
//                    if (task.isSuccessful) {
//                        val user = auth.currentUser
//                        Log.d(TAG, "signInWithCredential:success")
//                        saveUserData(user, it) // Save user data to Firestore
//                        updateUI(user)
//                    } else {
//                        Log.w(TAG, "signInWithCredential:failure", task.exception)
//                        updateUI(null)
//                    }
//                }
//        }
//    }
//    private fun saveUserData(user: FirebaseUser?, account: GoogleSignInAccount) {
//        user?.let {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    // Reference to the Firestore document
//                    val userDocumentRef = firestore.collection("users").document(user.uid)
//
//                    // Check if the document already exists
//                    val documentSnapshot = userDocumentRef.get().await()
//
//                    if (!documentSnapshot.exists()) {
//                        // Document does not exist, save the new user data
//                        val userData = User(
//                            uid = user.uid,
//                            displayName = account.displayName ?: "Unknown",
//                            photoUrl = account.photoUrl?.toString() ?: ""
//                        )
//
//                        userDocumentRef.set(userData).await()
//                        Log.d(TAG, "User data saved successfully")
//                    } else {
//                        Log.d(TAG, "User data already exists")
//                    }
//                } catch (e: Exception) {
//                    Log.e(TAG, "Error saving user data", e)
//                }
//            }
//        }
//    }
//
//
//    private fun updateUI(user: FirebaseUser?) {
//        if (user != null) {
//            Log.d(TAG, "User signed in: ${user.displayName}")
//            // Notify MainActivity to refresh menu
//            (activity as MainActivity).invalidateOptionsMenu()
//            // Navigate to studentsFragment
//            findNavController().navigate(R.id.action_loginFragment_to_studentsFragment)
//        } else {
//            Log.d(TAG, "No user signed in")
//            // Prompt user to try again or show a message
//        }
//    }
}
