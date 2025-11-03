package com.example.post5

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.post5.databinding.ActivityMainBinding

// Pastikan nama kelas sesuai dengan nama file
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var postAdapter: PostAdapter

    private val addEditPostLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val post = if (Build.VERSION.SDK_INT >= 33) {
                data?.getParcelableExtra(AddPostActivity.EXTRA_REPLY_POST, Post::class.java)
            } else {
                @Suppress("DEPRECATION")
                data?.getParcelableExtra(AddPostActivity.EXTRA_REPLY_POST)
            }

            post?.let {
                if (it.id == -1) { // -1 menandakan post baru
                    viewModel.addPost(it.username, it.caption, it.imageUri)
                } else { // Selain itu, ini adalah update
                    viewModel.updatePost(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupRecyclerViews()
        observeViewModel()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            addEditPostLauncher.launch(intent)
        }
    }

    private fun setupAdapters() {
        profileAdapter = ProfileAdapter(emptyList())
        postAdapter = PostAdapter(
            emptyList(),
            onEditClick = { post ->
                val intent = Intent(this, AddPostActivity::class.java).apply {
                    putExtra(AddPostActivity.EXTRA_POST, post)
                }
                addEditPostLauncher.launch(intent)
            },
            onDeleteClick = { post ->
                viewModel.deletePost(post)
                Toast.makeText(this, "Postingan dihapus!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupRecyclerViews() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = profileAdapter
        }

        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.profiles.observe(this) { profileList ->
            profileAdapter = ProfileAdapter(profileList)
            binding.rvStories.adapter = profileAdapter
        }

        viewModel.posts.observe(this) { postList ->
            postAdapter.submitList(postList)
        }
    }
}
