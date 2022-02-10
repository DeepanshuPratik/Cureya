package com.example.cureya.home

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.example.cureya.home.data.blog

class blogAdapter(private val listener: blogitemClicked, private val images:List<blog>): RecyclerView.Adapter<blogAdapter.BlogViewHolder>() {


    class BlogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(image: blog){
            blog_image.setImageResource(image.imageId)
            blog_name.text=image.name
        }
        val blog_name: TextView=itemView.findViewById(R.id.blog_name)
        val blog_image: ImageView= itemView.findViewById(R.id.blog_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.blog_item,parent,false)
        val viewHolder=BlogViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(images[viewHolder.bindingAdapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bindView(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
interface blogitemClicked{
    fun onItemClicked(item: blog)
}