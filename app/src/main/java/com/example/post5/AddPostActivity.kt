package com.example.post5

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.post5.databinding.ActivityAddPostBinding

// Pastikan nama kelas sesuai dengan nama file
class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private var selectedImageUri: Uri? = null
    private var editablePost: Post? = null

    companion object {
        const val EXTRA_POST = "extra_post"
        const val EXTRA_REPLY_POST = "extra_reply_post"
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.ivPreviewImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editablePost = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_POST, Post::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_POST)
        }

        setupView()

        binding.btnUploadImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveOrUpdatePost()
        }
    }

    private fun setupView() {
        editablePost?.let { post ->
            binding.tvPageTitle.text = "Edit Postingan"
            binding.btnSave.text = "Update"
            binding.etUsername.setText(post.username)
            binding.etCaption.setText(post.caption)
            selectedImageUri = post.imageUri
            Glide.with(this).load(post.imageUri).into(binding.ivPreviewImage)
        }
    }

    private fun saveOrUpdatePost() {
        val username = binding.etUsername.text.toString().trim()
        val caption = binding.etCaption.text.toString().trim()

        if (username.isEmpty() || caption.isEmpty()) {
            Toast.makeText(this, "Username dan caption tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Silakan pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        val replyIntent = Intent()
        val resultPost: Post

        if (editablePost == null) {
            resultPost = Post(-1, username, caption, selectedImageUri!!, 0)
            Toast.makeText(this, "Postingan berhasil dibuat!", Toast.LENGTH_SHORT).show()
        } else {
            resultPost = editablePost!!.copy(
                username = username,
                caption = caption,
                imageUri = selectedImageUri!!
            )
            Toast.makeText(this, "Postingan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        }

        replyIntent.putExtra(EXTRA_REPLY_POST, resultPost)
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }
}
