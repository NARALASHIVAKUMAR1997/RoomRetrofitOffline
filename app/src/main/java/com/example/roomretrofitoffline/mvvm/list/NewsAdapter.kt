package com.example.roomretrofitoffline.mvvm.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomretrofitoffline.R
import com.example.roomretrofitoffline.repositary.room.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_list_row_item.view.*

class NewsAdapter(
    var context: Context,
    var list: List<News>,
    private var clickListener: (view: View, position:Int) -> Unit
) : RecyclerView.Adapter<NewsAdapter.MYViewHolder>() {

    inner class MYViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { clickListener(it, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYViewHolder {
        return MYViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_news_list_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MYViewHolder, position: Int) {
        Picasso.get().load(list[position].urlToImage)
            .error(R.drawable.ic_baseline_image_24)
            .resize(100,100)
            .into(holder.itemView.news_url_image)
        holder.itemView.news_title.text = list[position].title
        holder.itemView.author.text = list[position].author
    }

    override fun getItemCount(): Int = list.size

    internal fun update(arrayList: List<News>) {
        this.list = arrayList
        notifyDataSetChanged()
        Log.v("adapter", list.toString())
    }
}