package com.example.post5

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.post5.Profile
import com.example.post5.databinding.ItemProfileBinding

class ProfileAdapter(private val profileList: List<Profile>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) {
            binding.ivProfileStory.setImageResource(profile.profileImageResId)
            binding.tvUsernameStory.text = profile.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        // Pastikan Anda menggunakan ViewBinding untuk item_profile.xml
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(profileList[position])
    }

    override fun getItemCount(): Int = profileList.size
}
