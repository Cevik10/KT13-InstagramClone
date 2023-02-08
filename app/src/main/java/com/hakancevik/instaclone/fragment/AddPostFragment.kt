package com.hakancevik.instaclone.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.databinding.FragmentAddPostBinding
import java.io.IOException
import java.util.UUID


class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedImage: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLauncher()

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding.imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // rationale
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                        // request permission
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()


                } else {
                    // request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }


            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }


        binding.uploadButton.setOnClickListener {

            binding.progressBarUpload.isVisible = true

            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"

            val reference = firebaseStorage.reference
            val imageReference = reference.child("images").child(imageName)

            if (selectedImage != null) {
                imageReference.putFile(selectedImage!!).addOnSuccessListener {

                    // download url -> firestore
                    val uploadedImageReference = firebaseStorage.reference.child("images").child(imageName)
                    uploadedImageReference.downloadUrl.addOnSuccessListener {

                        val downloadUrl = it.toString()
                        val comment = binding.commentEditText.text.toString().trim()
                        val email = auth.currentUser!!.email

                        val postMap = hashMapOf<String, Any>()
                        postMap.put("email",email!!)
                        postMap.put("imageUrl", downloadUrl)
                        postMap.put("comment", comment)
                        postMap.put("date", Timestamp.now())

                        firebaseFirestore.collection("Posts").add(postMap).addOnSuccessListener {

                            binding.progressBarUpload.isVisible = false
                            Toast.makeText(requireContext().applicationContext, "Successfully Saved!", Toast.LENGTH_LONG).show()
                            val action = AddPostFragmentDirections.actionAddPostFragmentToHomeFragment()
                            Navigation.findNavController(requireView()).navigate(action)


                        }.addOnFailureListener {
                            binding.progressBarUpload.isVisible = false
                            Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }


                    }.addOnFailureListener {
                        binding.progressBarUpload.isVisible = false
                        Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }


                }.addOnFailureListener {
                    binding.progressBarUpload.isVisible = false
                    Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }

        }


    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedImage = intentFromResult.data

                    selectedImage?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }

        }


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permission needed!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}