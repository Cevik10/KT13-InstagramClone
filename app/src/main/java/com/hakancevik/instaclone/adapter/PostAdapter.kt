package com.hakancevik.instaclone.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.instaclone.databinding.RecyclerRowBinding
import com.hakancevik.instaclone.model.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class PostAdapter(val postArrayList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        val timestamp = postArrayList.get(position).date
        val date = timestamp.toDate()
        val dateFormated = SimpleDateFormat("dd/MM/yyyy").format(date)


        holder.binding.recyclerViewEmailText.text = postArrayList[position].email
        holder.binding.recyclerViewCommentText.text = postArrayList[position].comment
        holder.binding.recyclerViewDateText.text = dateFormated

        Picasso.get().load(postArrayList[position].imageUrl).into(holder.binding.recyclerViewImageView)


    }


}