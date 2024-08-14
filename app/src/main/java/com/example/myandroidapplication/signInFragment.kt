package com.example.myandroidapplication;

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController


class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var signUpLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        signInButton = view.findViewById(R.id.signInButton)
        progressBar = view.findViewById(R.id.progressBar)
        signUpLink = view.findViewById(R.id.signUpLink)
        signUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        signInButton.setOnClickListener {
            signInUser()
        }

        return view
    }

    private fun signInUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign-in success, navigate to another fragment or activity
                    Toast.makeText(requireContext(), "Sign-in successful", Toast.LENGTH_SHORT).show()
                    Log.d("SignInFragment", "signInWithEmail:success")
                    findNavController().navigateUp()
                    // Navigate to the main fragment
                } else {
                    // If sign-in fails, display a message to the user.
                    Toast.makeText(requireContext(), "Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.w("SignInFragment", "signInWithEmail:failure", task.exception)
                }
            }
    }
}
