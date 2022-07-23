package com.example.scanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scanner.R
import com.example.scanner.common.loadImageFromNetwork
import com.example.scanner.databinding.ItemProductInCartBinding
import com.example.scanner.models.ProductModel
import java.text.DecimalFormat

class ProductCartAdapter(
    private val cartProductList: List<ProductModel>
): RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemProductInCartBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product_in_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productModel = cartProductList[position]
        holder.binding.apply {
            productImage.loadImageFromNetwork(productModel.id, productModel.productImageUrl)
            productTitle.text = productModel.productName
            productPrice.text = "\$${DecimalFormat("0.00").format(productModel.productPrice)}"
            productQty.text = "Qty: ${productModel.qty}"
        }
    }

    override fun getItemCount(): Int {
        return cartProductList.size
    }
}