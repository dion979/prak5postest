package com.example.post5

// com/example/post5/PostAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.post5.Post
import com.example.post5.R
import com.example.post5.databinding.ItemPostBinding

class PostAdapter(
    private var postList: List<Post>,
    private val onEditClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvUsernamePost.text = post.username
            binding.tvCaption.text = post.caption
            binding.ivProfilePost.setImageResource(post.profileImageResId)

            // Muat gambar postingan dari URI
            Glide.with(itemView.context)
                .load(post.imageUri)
                .into(binding.ivPostImage)

            // Setup menu titik tiga
            binding.ivMenu.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menuInflater.inflate(R.menu.post_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> {
                            onEditClick(post)
                            true
                        }
                        R.id.menu_delete -> {
                            onDeleteClick(post)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    // Fungsi untuk update data di adapter
    fun submitList(newPosts: List<Post>) {
        postList = newPosts
        notifyDataSetChanged() // Gunakan DiffUtil di proyek nyata untuk performa lebih baik
    }
}
