package com.hakancevik.instaclone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {


    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

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

        binding.signUpLayout.setOnClickListener(View.OnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}