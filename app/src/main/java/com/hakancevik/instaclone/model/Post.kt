package com.hakancevik.instaclone.model

import com.google.firebase.Timestamp

data class Post(val email: String, val comment: String, val imageUrl: String, val date: Timestamp)