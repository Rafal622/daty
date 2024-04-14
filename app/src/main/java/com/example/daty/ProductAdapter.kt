package com.example.daty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val context: Context, private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indexTextView: TextView = itemView.findViewById(R.id.indexTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val expiryDateTextView: TextView = itemView.findViewById(R.id.expiryDateTextView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.indexTextView.text = context.getString(R.string.index_format, position + 1)
        holder.nameTextView.text = product.name
        holder.expiryDateTextView.text = product.expiryDate
    }
}