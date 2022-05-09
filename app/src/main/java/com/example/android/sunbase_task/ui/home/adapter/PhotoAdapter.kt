package com.example.android.sunbase_task.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.sunbase_task.data.model.Photoss
import com.example.android.sunbase_task.databinding.HomeFragmentRecyclerviewLayoutBinding
import com.example.android.sunbase_task.utils.getCircularProgressBar

class PhotoAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<Photoss, PhotoAdapter.PhotoViewHolder>(
        PhotoDiffCallback()
    ) {


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {

        return PhotoViewHolder(
            HomeFragmentRecyclerviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class PhotoViewHolder(
        private val binding: HomeFragmentRecyclerviewLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Photoss?) {

            binding.let {
                /* it.root.setOnClickListener {
                     clicked.invoke(name)
                 }*/

                Glide.with(it.root.context).load(data?.urls?.thumb)
                    .apply(RequestOptions()
                        .placeholder(getCircularProgressBar(it.root.context))
                    )
                    .into(it.imageView)

            }

        }
    }

    private class PhotoDiffCallback : DiffUtil.ItemCallback<Photoss>() {
        override fun areItemsTheSame(oldItem: Photoss, newItem: Photoss): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photoss, newItem: Photoss): Boolean {
            return oldItem == newItem
        }
    }

}