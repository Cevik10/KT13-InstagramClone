package com.hakancevik.instaclone.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hakancevik.instaclone.R
import com.hakancevik.instaclone.databinding.BottomSheetProgressBarBinding

class ProgressBarBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: BottomSheetProgressBarBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG: String = "ProgressBarBottomDialogFragment"

        fun newInstance(): Any {
            return ProgressBarBottomDialogFragment()
        }
    }

    private var mListener: ItemClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.bottom_sheet_upload_image, container, false)
        _binding = BottomSheetProgressBarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getTheme(): Int {
        return R.style.ProgressBarDialogTheme
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

    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }


}