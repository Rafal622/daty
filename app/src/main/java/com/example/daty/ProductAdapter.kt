package com.example.daty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val context: Context, private var dataList: List<Data>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val expiryDateTextView: TextView = itemView.findViewById(R.id.expiryDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
        holder.expiryDateTextView.text = currentItem.date.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateDataList(newDataList: List<Data>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}