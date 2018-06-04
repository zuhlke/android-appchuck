package com.zuhlke.chucknorris.categorylist;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zuhlke.chucknorris.R

class CategoryListAdapter(private val list: List<String>)
    : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(view: View) {

        }

        val categoryName: TextView = view.findViewById(R.id.tv_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        val viewHolder = ViewHolder(itemView)

        itemView.setOnClickListener(viewHolder)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.text = list[position]
    }

    override fun getItemCount() = list.size

}