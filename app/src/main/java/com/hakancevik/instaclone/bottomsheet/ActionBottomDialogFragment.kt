package com.hakancevik.instaclone.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.databinding.BottomSheetUploadImageBinding
import com.hakancevik.instaclone.fragment.AddPostFragment

import java.util.*


class ActionBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {


    private var _binding: BottomSheetUploadImageBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG: String = "ActionBottomDialogFragment"

        fun newInstance(): Any {
            return ActionBottomDialogFragment()
        }
    }


    private var mListener: ItemClickListener? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    private lateinit var progressBarDialog: ProgressBarBottomDialogFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.bottom_sheet_upload_image, container, false)
        _binding = BottomSheetUploadImageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomSheetSaveButton.setOnClickListener(this)
        binding.bottomSheetCancelButton.setOnClickListener(this)


        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        progressBarDialog = ProgressBarBottomDialogFragment.newInstance() as ProgressBarBottomDialogFragment

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context as ItemClickListener
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bottomSheetSaveButton -> {

                val descriptionText = binding.bottomSheetDescriptionText.text.toString().trim()
                if (descriptionText.isEmpty()) {
                    binding.bottomSheetDescriptionText.error = "Specify description message"
                    binding.bottomSheetDescriptionText.requestFocus()
                    return
                }

                showProgressBarDialog()

                val uuid = UUID.randomUUID()
                val imageName = "${uuid}.jpg"

                val reference = firebaseStorage.reference
                val imageReference = reference.child("images").child(imageName)

                if (AddPostFragment.selectedImage != null) {
                    imageReference.putFile(AddPostFragment.selectedImage!!).addOnSuccessListener {

                        // download url -> firestore
                        val uploadedImageReference = firebaseStorage.reference.child("images").child(imageName)
                        uploadedImageReference.downloadUrl.addOnSuccessListener {

                            val downloadUrl = it.toString()
                            val comment = descriptionText
                            val email = auth.currentUser!!.email

                            val postMap = hashMapOf<String, Any>()
                            postMap["email"] = email!!
                            postMap["imageUrl"] = downloadUrl
                            postMap["comment"] = comment
                            postMap["date"] = Timestamp.now()

                            firebaseFirestore.collection("Posts").add(postMap).addOnSuccessListener {

                                dismissProgressBarDialog()
                                Toast.makeText(requireContext().applicationContext, "Successfully Saved!", Toast.LENGTH_SHORT).show()

                                onDestroyView()
//                                val action = AddPostFragmentDirections.actionAddPostFragmentToHomeFragment()
//                                Navigation.findNavController(requireView()).navigate(action)


                            }.addOnFailureListener {
                                dismissProgressBarDialog()
                                Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                            }


                        }.addOnFailureListener {
                            dismissProgressBarDialog()
                            Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                        }


                    }.addOnFailureListener {
                        dismissProgressBarDialog()
                        Toast.makeText(requireContext().applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
            R.id.bottomSheetCancelButton -> {
                onDestroyView()
            }

        }


    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }


    private fun showProgressBarDialog() {
        progressBarDialog.show(requireActivity().supportFragmentManager, ProgressBarBottomDialogFragment.TAG)
    }

    private fun dismissProgressBarDialog() {
        progressBarDialog.dismiss()
    }


    override fun onStart() {
        super.onStart()

        val imageView = activity?.findViewById<ImageView>(R.id.imageView)
        imageView?.layoutParams?.height = 1000
        imageView?.layoutParams?.width = 1000
        imageView?.requestLayout()



        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val bottomSheetBehavior = BottomSheetBehavior.from(it)
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    //dialog?.window?.setDimAmount((1 - newState).toFloat())

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    imageView?.layoutParams?.height = 500
                    imageView?.layoutParams?.width = 500
                    imageView?.requestLayout()


                }

            })

        }


    }


}
