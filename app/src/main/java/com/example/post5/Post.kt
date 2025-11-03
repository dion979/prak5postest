package com.example.post5

// com/example/post5/Post.kt
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Int,
    var username: String,
    var caption: String,
    var imageUri: Uri, // URI untuk gambar postingan
    val profileImageResId: Int // Resource ID untuk foto profil
) : Parcelable
