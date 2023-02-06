package com.hakancevik.instaclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.activity.FeedActivity
import com.hakancevik.instaclone.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {


    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            intentToFeedActivity()
        }


        binding.signUpLayout.setOnClickListener(View.OnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        })

        binding.loginButton.setOnClickListener(View.OnClickListener {


            val userEmail = binding.emailText.text.toString().trim()
            val userPassword = binding.passwordText.text.toString().trim()

            if (userEmail.isEmpty()) {
                binding.emailText.error = "E-mail is required!"
                binding.emailText.requestFocus()
                return@OnClickListener
            }

            if (userPassword.isEmpty()) {
                binding.passwordText.error = "Password is required!"
                binding.passwordText.requestFocus()
                return@OnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false

            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {
                binding.loginButton.isEnabled = true
                binding.progressBar.visibility = View.GONE
                intentToFeedActivity()

            }.addOnFailureListener {
                Toast.makeText(requireActivity().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.loginButton.isEnabled = true
            }


        })

    }

    private fun intentToFeedActivity() {
        val intent = Intent(activity, FeedActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}