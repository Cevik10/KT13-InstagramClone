package com.hakancevik.instaclone.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hakancevik.instaclone.activity.FeedActivity
import com.hakancevik.instaclone.databinding.FragmentSignUpBinding
import com.hakancevik.instaclone.model.User

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private var firebaseFirestore: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()


        binding.createAccountButton.setOnClickListener(View.OnClickListener {


            val name = binding.fullNameText.text.toString().trim()
            val age = binding.ageText.text.toString().trim()
            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()

            if (name.isEmpty()) {
                binding.fullNameText.error = "Full name is required!"
                binding.fullNameText.requestFocus()
                return@OnClickListener
            }

            if (age.isEmpty()) {
                binding.ageText.error = "Age is required!"
                binding.ageText.requestFocus()
                return@OnClickListener
            }

            if (email.isEmpty()) {
                binding.emailText.error = "E-mail is required!"
                binding.emailText.requestFocus()
                return@OnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordText.error = "Password is required!"
                binding.passwordText.requestFocus()
                return@OnClickListener
            }

            if (password.length < 6) {
                binding.passwordText.error = "Min password length should be 6 characters!"
                binding.passwordText.requestFocus()
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.createAccountButton.isEnabled = false

            auth!!.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val user = User(name, age, email)
                val userAuth = HashMap<String, Any>()
                userAuth["FullName"] = user.fullName
                userAuth["Age"] = user.age
                userAuth["Email"] = user.email


                firebaseFirestore!!.collection("Users").document(auth!!.currentUser!!.uid).set(userAuth).addOnSuccessListener {

                    Toast.makeText(requireActivity().applicationContext, "Account has been created successfully!", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.createAccountButton.isEnabled = true

                    val intent = Intent(activity,FeedActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()


                }.addOnFailureListener {
                    Toast.makeText(requireActivity().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.createAccountButton.isEnabled = true

                }


            }.addOnFailureListener {
                Toast.makeText(requireActivity().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.createAccountButton.isEnabled = true
            }


        })


        binding.logInLayout.setOnClickListener(View.OnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}