package com.dicoding.picodiploma.loginwithanimation.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.story.detailstory.DetailStoryActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.meone.storyapp.utils.addEllipsisAfter10Words

class ListStoryAdapter (private val listStory : List<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_story, parent, false)
        return ListViewHolder(view)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.ivStory)
        private var tvName :TextView = itemView.findViewById(R.id.tvTitle)
        private var tvDescription:TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(item: ListStoryItem) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(imgPhoto)
            tvName.text = item.name
            tvDescription.text = item.description.addEllipsisAfter10Words()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("item", item)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size




}