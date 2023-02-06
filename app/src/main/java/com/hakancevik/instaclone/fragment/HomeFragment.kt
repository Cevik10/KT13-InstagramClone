package com.hakancevik.instaclone.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.activity.FeedActivity
import com.hakancevik.instaclone.activity.MainActivity
import com.hakancevik.instaclone.databinding.FragmentHomeBinding
import org.checkerframework.checker.units.qual.Length


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.options_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_post_item -> {
                val action = HomeFragmentDirections.actionHomeFragmentToAddPostFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            R.id.log_out_item -> {
                auth.signOut()
                val intentToMain = Intent(activity, MainActivity::class.java)
                startActivity(intentToMain)
                requireActivity().finish()
            }
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}