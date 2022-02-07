package com.example.colorimagesearch.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagesearch.R
import com.example.colorimagesearch.database.FavoriteList
import com.example.colorimagesearch.databinding.ColorsListItemBinding
import com.example.colorimagesearch.model.Colors
import com.example.colorimagesearch.ui.MainActivity
import kotlinx.android.extensions.LayoutContainer

class ColorsListAdapter() : RecyclerView.Adapter<ColorsListAdapter.ViewHolder>() {

    private var mLists: List<Colors>? = listOf()

    fun setData(lists: List<Colors>) {
        mLists = lists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ColorsListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.colors_list_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mLists!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemBinding.colors = mLists!![position]
        val id = mLists!![position].id.toInt()
        holder.title.text = "Title: " + mLists!![position].title
        holder.hex.text = "Hex: " + mLists!![position].hex
        if (id?.let { MainActivity.daoContact!!.isFavorite(it) } == 1) {
            holder.imgViewLikeUnlike.setImageResource(R.drawable.ic_baseline_favorite)
        } else {
            holder.imgViewLikeUnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mLists!![position].url))
                MainActivity.mContext!!.startActivity(myIntent)
            }
        })

        holder.imgViewLikeUnlike.setOnClickListener {
            val favoriteList = FavoriteList()
            val id = mLists!![position].id?.toInt()
            val colorsId = mLists!![position].id?.toString()
            if (id != null) {
                favoriteList.id = id
            }
            favoriteList.colorsId = colorsId
            if (id?.let { it1 -> MainActivity.daoContact!!.isFavorite(it1) } != 1) {
                holder.imgViewLikeUnlike.setImageResource(R.drawable.ic_baseline_favorite)
                MainActivity.daoContact!!.addData(favoriteList)
            } else {
                holder.imgViewLikeUnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                MainActivity.daoContact!!.delete(favoriteList)
            }
        }
    }

    class ViewHolder(var itemBinding: ColorsListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), LayoutContainer {
        override val containerView: View?
            get() = itemBinding.root

        val imgViewLikeUnlike = itemView.findViewById(R.id.imgViewLikeUnLike) as ImageView
        val title = itemView.findViewById(R.id.titleTv) as TextView
        val hex = itemView.findViewById(R.id.descriptionTv) as TextView
    }
}