package com.example.post5

// com/example/post5/PostViewModel.kt
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.post5.R

class PostViewModel : ViewModel() {

    // --- Data untuk Postingan ---
    private val _posts = MutableLiveData<MutableList<Post>>()
    val posts: LiveData<MutableList<Post>> get() = _posts

    // --- Data untuk Profil/Story ---
    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private var nextPostId = 0

    init {
        // Data awal untuk profil (story)
        _profiles.value = listOf(
            Profile("prabowo", R.drawable.prabowo),
            Profile("jokowi", R.drawable.jokowi),
            Profile("anies", R.drawable.anies),
            Profile("bahlil", R.drawable.bahlil),
            Profile("sahroni", R.drawable.sahroni)
        )
        // Data awal untuk postingan (kosong)
        _posts.value = mutableListOf()
    }

    // Fungsi untuk mendapatkan resource gambar profil berdasarkan username
    private fun getProfileImage(username: String) : Int {
        return profiles.value?.find { it.username.equals(username, ignoreCase = true) }?.profileImageResId ?: R.drawable.ic_placeholder_image
    }

    fun addPost(username: String, caption: String, imageUri: Uri) {
        val newPost = Post(
            id = nextPostId++,
            username = username,
            caption = caption,
            imageUri = imageUri,
            profileImageResId = getProfileImage(username)
        )
        val currentList = _posts.value ?: mutableListOf()
        currentList.add(0, newPost) // Tambah di paling atas
        _posts.value = currentList
    }

    fun updatePost(updatedPost: Post) {
        val currentList = _posts.value ?: return
        val index = currentList.indexOfFirst { it.id == updatedPost.id }
        if (index != -1) {
            // Pastikan gambar profil juga terupdate jika username berubah
            val postWithCorrectProfile = updatedPost.copy(profileImageResId = getProfileImage(updatedPost.username))
            currentList[index] = postWithCorrectProfile
            _posts.value = currentList
        }
    }

    fun deletePost(postToDelete: Post) {
        val currentList = _posts.value ?: return
        currentList.remove(postToDelete)
        _posts.value = currentList
    }
}
